package com.isp.backend.domain.gpt.dto;

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
    private List<Choice> choices;

    @Getter
    @Setter
    public static class Choice {
        private GptMessage message;
    }
}
