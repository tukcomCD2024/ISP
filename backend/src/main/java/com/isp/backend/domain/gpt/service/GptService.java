package com.isp.backend.domain.gpt.service;

import com.isp.backend.domain.gpt.config.GptConfig;
import com.isp.backend.domain.gpt.dto.GptRequestDTO;
import com.isp.backend.domain.gpt.dto.GptResponseDTO;
import com.isp.backend.domain.gpt.dto.GptScheduleRequestDto;
import com.isp.backend.domain.gpt.entity.GptMessage;
import com.isp.backend.domain.gpt.mapper.GptMapper;
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
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GptService {
    private final RestTemplate restTemplate;
    private final GptMapper gptMapper;

    @Value("${api-key.chat-gpt}")
    private String apiKey;

    public HttpEntity<GptRequestDTO> buildHttpEntity(GptRequestDTO gptRequestDTO) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(GptConfig.MEDIA_TYPE));
        httpHeaders.add(GptConfig.AUTHORIZATION, GptConfig.BEARER + apiKey);
        return new HttpEntity<>(gptRequestDTO, httpHeaders);
    }

    public GptResponseDTO getResponse(HttpEntity<GptRequestDTO> chatGptRequestHttpEntity) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(60000);
        requestFactory.setReadTimeout(60 * 1000);
        restTemplate.setRequestFactory(requestFactory);

        ResponseEntity<GptResponseDTO> responseEntity = restTemplate.postForEntity(
                GptConfig.CHAT_URL,
                chatGptRequestHttpEntity,
                GptResponseDTO.class);

        return responseEntity.getBody();
    }

    public GptResponseDTO askQuestion(GptScheduleRequestDto questionRequestDTO) {
        List<GptMessage> messages = new ArrayList<>();
        String question = questionRequestDTO.getDepartureDate();
        messages.add(GptMessage.builder()
                .role(GptConfig.ROLE)
                .content(question)
                .build());
        return this.getResponse(
                this.buildHttpEntity(
                        new GptRequestDTO(
                                GptConfig.CHAT_MODEL,
                                GptConfig.MAX_TOKEN,
                                GptConfig.TEMPERATURE,
                                GptConfig.STREAM,
                                messages
                        )
                )
        );
    }
}