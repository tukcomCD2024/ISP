package com.isp.backend.domain.gpt.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class GptMessage implements Serializable {
    private String role;
    private String content;

    @Builder
    public GptMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
