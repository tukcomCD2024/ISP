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

@Service
@RequiredArgsConstructor
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

        List<GptSchedule> gptSchedules = gptScheduleParser.parseScheduleText(extractScheduleText(response));
        return new GptScheduleResponse(gptSchedules);
    }

    private String extractScheduleText(GptResponse gptResponse) {
        return gptResponse.getChoices().get(0).getMessage().getContent();
    }

    @Async
    public CompletableFuture<GptSchedulesResponse> askQuestion(GptScheduleRequest questionRequest) {
        String question = formatQuestion(questionRequest);
        List<GptMessage> messages = Collections.singletonList(createMessage(question));
        Country country = scheduleService.validateCountry(questionRequest.getDestination());
        String countryImage = country.getImageUrl();

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<CompletableFuture<GptScheduleResponse>> futures = Arrays.asList(
                sendRequestAsync(messages, executorService),
                sendRequestAsync(messages, executorService),
                sendRequestAsync(messages, executorService)
        );

        CompletableFuture<List<GptScheduleResponse>> combinedFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));

        return combinedFuture.thenApply(schedules -> new GptSchedulesResponse(countryImage, schedules));
    }

    private CompletableFuture<GptScheduleResponse> sendRequestAsync(List<GptMessage> messages, ExecutorService executor) {
        HttpHeaders headers = buildHttpHeaders(apiKey);
        GptRequest request = createGptRequest(messages);
        return CompletableFuture.supplyAsync(() -> getResponse(headers, request), executor);
    }

    private GptRequest createGptRequest(List<GptMessage> messages) {
        return new GptRequest(GptConfig.CHAT_MODEL, GptConfig.MAX_TOKEN, GptConfig.TEMPERATURE, GptConfig.STREAM, messages);
    }

    private HttpHeaders buildHttpHeaders(String key) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, GptConfig.BEARER + key);
        return headers;
    }

    private String formatQuestion(GptScheduleRequest questionRequest) {
        return String.format(GptConfig.PROMPT,
                questionRequest.getDestination(),
                questionRequest.getPurpose(),
                questionRequest.getIncludedActivities(),
                questionRequest.getExcludedActivities(),
                questionRequest.getDepartureDate(),
                questionRequest.getReturnDate(),
                String.join(ParsingConstants.COMMA, questionRequest.getPurpose())
        );
    }

    private GptMessage createMessage(String content) {
        return GptMessage.builder()
                .role(GptConfig.ROLE)
                .content(content)
                .build();
    }
}