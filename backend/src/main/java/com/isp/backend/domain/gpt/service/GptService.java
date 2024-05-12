package com.isp.backend.domain.gpt.service;

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
import com.isp.backend.global.s3.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GptService {
    private final RestTemplate restTemplate;
    private final GptScheduleParser gptScheduleParser;
    private final S3ImageService s3ImageService;

    @Value("${api-key.chat-gpt}")
    private String apiKey;

    public HttpEntity<GptRequest> buildHttpEntity(GptRequest gptRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(GptConfig.MEDIA_TYPE));
        httpHeaders.add(GptConfig.AUTHORIZATION, GptConfig.BEARER + apiKey);
        return new HttpEntity<>(gptRequest, httpHeaders);
    }

    public GptScheduleResponse getResponse(HttpEntity<GptRequest> chatGptRequestHttpEntity) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(60000);
        requestFactory.setReadTimeout(60 * 1000);
        restTemplate.setRequestFactory(requestFactory);

        ResponseEntity<GptResponse> responseEntity = restTemplate.postForEntity(
                GptConfig.CHAT_URL,
                chatGptRequestHttpEntity,
                GptResponse.class);
        List<GptSchedule> gptSchedules = gptScheduleParser.parseScheduleText(getScheduleText(responseEntity));

        return new GptScheduleResponse(gptSchedules);
    }

    private String getScheduleText(ResponseEntity<GptResponse> responseEntity) {
        return getGptMessage(responseEntity).toString();
    }

    private GptMessage getGptMessage(ResponseEntity<GptResponse> responseEntity) {
        return responseEntity.getBody().getChoices().get(0).getMessage();
    }

    public GptSchedulesResponse askQuestion(GptScheduleRequest questionRequestDTO) {
        String question = makeQuestion(questionRequestDTO);
        List<GptMessage> messages = Collections.singletonList(
                GptMessage.builder()
                        .role(GptConfig.ROLE)
                        .content(question)
                        .build()
        );

        String countryImage = s3ImageService.get(questionRequestDTO.getDestination());
        List<GptScheduleResponse> schedules = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            schedules.add(
                    this.getResponse(
                            this.buildHttpEntity(
                                    new GptRequest(
                                        GptConfig.CHAT_MODEL,
                                        GptConfig.MAX_TOKEN,
                                        GptConfig.TEMPERATURE,
                                        GptConfig.STREAM,
                                        messages
                                )
                        )
                ));
        }




        return new GptSchedulesResponse(countryImage, schedules);
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