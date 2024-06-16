package com.isp.backend.domain.gpt.service;

import com.isp.backend.domain.country.entity.Country;
import com.isp.backend.domain.gpt.config.GptConfig;
import com.isp.backend.domain.gpt.constant.ParsingConstants;
import com.isp.backend.domain.gpt.dto.request.GptRequest;
import com.isp.backend.domain.gpt.dto.request.GptScheduleRequest;
import com.isp.backend.domain.gpt.dto.response.GptResponse;
import com.isp.backend.domain.gpt.dto.response.GptScheduleResponse;
import com.isp.backend.domain.gpt.dto.response.GptSchedulesResponse;
import com.isp.backend.domain.gpt.entity.GptMessage;
import com.isp.backend.domain.gpt.entity.GptSchedule;
import com.isp.backend.domain.gpt.entity.GptScheduleParser;
import com.isp.backend.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GptService {
    private final GptScheduleParser gptScheduleParser;
    private final ScheduleService scheduleService;
    private final WebClient webClient;

    @Value("${api-key.gpt-trip}")
    private String apiKey;

    public GptScheduleResponse getResponse(HttpHeaders headers, GptRequest gptRequest) {

        GptResponse response = webClient.post()
                .uri(GptConfig.CHAT_URL)
                .headers(h -> h.addAll(headers))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(gptRequest))
                .retrieve()
                .bodyToMono(GptResponse.class)
                .block();

        List<GptSchedule> gptSchedules = gptScheduleParser.parseScheduleText(getScheduleText(response));
        return new GptScheduleResponse(gptSchedules);
    }

    private String getScheduleText(GptResponse gptResponse) {
        return getGptMessage(gptResponse).toString();
    }

    private GptMessage getGptMessage(GptResponse gptResponse) {
        return gptResponse.getChoices().get(0).getMessage();
    }

    @Async
    public CompletableFuture<GptSchedulesResponse> askQuestion(GptScheduleRequest questionRequestDTO) {
        String question = makeQuestion(questionRequestDTO);
        List<GptMessage> messages = Collections.singletonList(
                GptMessage.builder()
                        .role(GptConfig.ROLE)
                        .content(question)
                        .build()
        );

        Country country = scheduleService.validateCountry(questionRequestDTO.getDestination());
        String countryImage = country.getImageUrl();

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<CompletableFuture<GptScheduleResponse>> futures = Arrays.asList(
                sendRequestAsync(apiKey, messages, executorService),
                sendRequestAsync(apiKey, messages, executorService),
                sendRequestAsync(apiKey, messages, executorService)
        );

        CompletableFuture<List<GptScheduleResponse>> combinedFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));

        return combinedFuture.thenApply(schedules -> new GptSchedulesResponse(countryImage, schedules));
    }

    private CompletableFuture<GptScheduleResponse> sendRequestAsync(String apiKey, List<GptMessage> messages, ExecutorService executor) {
        HttpHeaders headers = buildHttpHeaders(apiKey);
        GptRequest request = getGptRequest(messages);
        return CompletableFuture.supplyAsync(() -> getResponse(headers, request), executor);
    }

    private GptRequest getGptRequest(List<GptMessage> messages) {
        return new GptRequest(
                GptConfig.CHAT_MODEL,
                GptConfig.MAX_TOKEN,
                GptConfig.TEMPERATURE,
                GptConfig.STREAM,
                messages
        );
    }

    private HttpHeaders buildHttpHeaders(String key) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(GptConfig.AUTHORIZATION, GptConfig.BEARER + key);
        return httpHeaders;
    }

    private String makeQuestion(GptScheduleRequest questionRequestDTO) {
        return String.format(GptConfig.PROMPT,
                questionRequestDTO.getDestination(),
                questionRequestDTO.getPurpose(),
                questionRequestDTO.getIncludedActivities(),
                questionRequestDTO.getExcludedActivities(),
                questionRequestDTO.getDepartureDate(),
                questionRequestDTO.getReturnDate(),
                String.join(ParsingConstants.COMMA, questionRequestDTO.getPurpose())
        );
    }
}