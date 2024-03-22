package com.isp.backend.domain.gpt.controller;

import com.isp.backend.domain.gpt.dto.request.GptScheduleRequest;
import com.isp.backend.domain.gpt.dto.response.GptScheduleResponse;
import com.isp.backend.domain.gpt.service.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/gpt")
@RestController
public class GptController {
    private final GptService gptService;

    @PostMapping("/create")
    public ResponseEntity<GptScheduleResponse> sendQuestion(@RequestBody GptScheduleRequest gptScheduleRequest) {
        GptScheduleResponse gptScheduleResponse = gptService.askQuestion(gptScheduleRequest);
        return ResponseEntity.ok(gptScheduleResponse);
    }
}