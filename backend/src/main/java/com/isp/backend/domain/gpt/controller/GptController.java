package com.isp.backend.domain.gpt.controller;

import com.isp.backend.domain.gpt.dto.GptScheduleRequestDto;
import com.isp.backend.domain.gpt.dto.GptScheduleResponseDto;
import com.isp.backend.domain.gpt.service.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/chat-gpt")
@RestController
public class GptController {
    private final GptService gptService;

    @PostMapping("/question")
    public ResponseEntity<GptScheduleResponseDto> sendQuestion(@RequestBody GptScheduleRequestDto gptScheduleRequestDto) {
        GptScheduleResponseDto gptScheduleResponseDto = gptService.askQuestion(gptScheduleRequestDto);
        return ResponseEntity.ok(gptScheduleResponseDto);
    }
}