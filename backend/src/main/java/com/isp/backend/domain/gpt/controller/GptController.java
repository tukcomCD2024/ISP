package com.isp.backend.domain.gpt.controller;

import com.isp.backend.domain.gpt.dto.request.GptScheduleRequest;
import com.isp.backend.domain.gpt.dto.response.GptScheduleResponse;
import com.isp.backend.domain.gpt.service.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/gpt")
@RestController
@ResponseStatus(value = HttpStatus.CREATED)
public class GptController {
    private final GptService gptService;

    @PostMapping("/schedules")
    public GptScheduleResponse sendQuestion(@RequestBody GptScheduleRequest gptScheduleRequest) {
        return gptService.askQuestion(gptScheduleRequest);
    }
}