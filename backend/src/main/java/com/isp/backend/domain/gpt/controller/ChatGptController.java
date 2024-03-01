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
@RequestMapping("/chat-gpt")
@RestController
public class ChatGptController {
    private final ChatGptService chatGptService;

    @PostMapping("/question")
    public ResponseEntity<ChatGptMessage> sendQuestion(@RequestBody QuestionRequestDTO questionRequestDTO) {
        String code = "";
        ChatGptResponseDTO chatGptResponseDTO = new ChatGptResponseDTO();
        try {
            chatGptResponseDTO = chatGptService.askQuestion(questionRequestDTO);
        } catch (Exception e) {
            code = e.getMessage();
        }
        //return 부분은 자유롭게 수정하시면됩니다. ex)return chatGptResponse;
        return ResponseEntity.ok(chatGptResponseDTO.getChoices().get(0).getMessage());
    }
}