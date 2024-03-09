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
        String question = makeQuestion(questionRequestDTO);
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

    private String makeQuestion(GptScheduleRequestDto questionRequestDTO) {
        return String.format(
                """
                        I would like you to plan a package tour program to plan your travel itinerary.Destination: %s
                        Purpose of travel: %s
                        Departure date: %s
                        Entry date: %s
                        The flight schedule is as follows.
                        
                        Also, plan your itinerary based on the travel distance and famous tourist destinations.
                        If possible, create the schedule in one-hour intervals.
                        Make sure to plan for at least 10 hours of activity each day.
                        
                        I would like the format to be as follows:
                        For example
                        ---
                        day 1: 2024-02-06
                        1. Go to location
                        2. See the place
                        3. Eat the Lunch
                        4. Go to location
                        5. See the place
                        6. Eat the dinner
                        7. shopping
                        
                        day 2: 2024-02-07
                        1. Go to location
                        2. See the place
                        3. Eat the Lunch
                        4. Go to location
                        5. See the place
                        6. Eat the dinner
                        7. shopping
                        
                        day 3: 2024-02-08
                        1. Go to location
                        2. See the place
                        3. Eat the Lunch
                        4. Go to location
                        5. See the place
                        6. Eat the dinner
                        7. shopping
                        ---
                        No need to say anything else, just plan your schedule right away.
                        Please create the result in Korean.""",
                questionRequestDTO.getDestination(),
                questionRequestDTO.getPurpose(),
                questionRequestDTO.getDepartureDate(),
                questionRequestDTO.getReturnDate()
        );
    }
}