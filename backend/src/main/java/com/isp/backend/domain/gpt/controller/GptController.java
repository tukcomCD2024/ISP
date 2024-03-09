package com.isp.backend.domain.gpt.controller;

import com.isp.backend.domain.gpt.dto.GptResponseDTO;
import com.isp.backend.domain.gpt.dto.GptScheduleRequestDto;
import com.isp.backend.domain.gpt.entity.GptMessage;
import com.isp.backend.domain.gpt.service.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("api/chat-gpt")
@RestController
public class GptController {
    private final GptService gptService;

    @PostMapping("/question")
    public ResponseEntity<GptMessage> sendQuestion(@RequestBody GptScheduleRequestDto gptScheduleRequestDto) {
        GptResponseDTO gptResponseDTO = gptService.askQuestion(gptScheduleRequestDto);
        return ResponseEntity.ok(gptResponseDTO.getChoices().get(0).getMessage());
    }
}