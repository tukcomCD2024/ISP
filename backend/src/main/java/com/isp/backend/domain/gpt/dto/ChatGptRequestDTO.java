package com.isp.backend.domain.gpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.isp.backend.domain.gpt.entity.ChatGptMessage;
import lombok.Builder;

import java.util.List;

public class ChatGptRequestDTO {
    private String model;
    @JsonProperty("max_tokens")
    private Integer maxTokens;
    private Double temperature;
    private Boolean stream;
    private List<ChatGptMessage> messages;

    @Builder
    public ChatGptRequestDTO(String model, Integer maxTokens, Double temperature,
                             Boolean stream, List<ChatGptMessage> messages
            /*,Double topP*/) {
        this.model = model;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.stream = stream;
        this.messages = messages;
    }
}
