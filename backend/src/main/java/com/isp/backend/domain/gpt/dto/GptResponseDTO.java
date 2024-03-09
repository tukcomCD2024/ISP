package com.isp.backend.domain.gpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.isp.backend.domain.gpt.entity.GptMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GptResponseDTO {
    private String id;
    private String object;
    private long created;
    private String model;
    private Usage usage;
    private List<Choice> choices;

    @Getter
    @Setter
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;
        @JsonProperty("completion_tokens")
        private int completionTokens;
        @JsonProperty("total_tokens")
        private int totalTokens;
    }

    @Getter
    @Setter
    public static class Choice {
        private GptMessage message;
        @JsonProperty("finish_reason")
        private String finishReason;
        private int index;
    }
}
