package com.isp.backend.domain.gpt.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.isp.backend.domain.gpt.entity.GptMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GptRequest {
    private String model;

    @JsonProperty("max_tokens")
    private Integer maxTokens;
    private Double temperature;
    private Boolean stream;
    private List<GptMessage> messages;
}
