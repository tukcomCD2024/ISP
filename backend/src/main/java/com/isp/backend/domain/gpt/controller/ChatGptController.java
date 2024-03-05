package com.isp.backend.domain.gpt.controller;

import com.isp.backend.domain.gpt.dto.ChatGptResponseDTO;
import com.isp.backend.domain.gpt.dto.QuestionRequestDTO;
import com.isp.backend.domain.gpt.entity.ChatGptMessage;
import com.isp.backend.domain.gpt.service.ChatGptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("api/chat-gpt")
@RestController
public class ChatGptController {
    private final ChatGptService chatGptService;

    @PostMapping("/question")
    public ResponseEntity<ChatGptMessage> sendQuestion(@RequestBody QuestionRequestDTO questionRequestDTO) {
        ChatGptResponseDTO chatGptResponseDTO = new ChatGptResponseDTO();
        chatGptResponseDTO = chatGptService.askQuestion(questionRequestDTO);
        return ResponseEntity.ok(chatGptResponseDTO.getChoices().get(0).getMessage());
    }
}