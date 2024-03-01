package com.isp.backend.domain.gpt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatGptMessage implements Serializable {
    private String role;
    private String content;
}
