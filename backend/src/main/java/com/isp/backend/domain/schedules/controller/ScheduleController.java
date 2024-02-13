package com.isp.backend.domain.schedules.controller;

import com.isp.backend.domain.schedules.dto.SchedulesRequestDTO;
import com.isp.backend.domain.schedules.service.ScheduleService;
import com.isp.backend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {


    private final ScheduleService scheduleService ;
    @PostMapping("/save")
    public ResponseEntity<Void> saveSchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                             @RequestBody SchedulesRequestDTO requestDTO) {
        scheduleService.saveSchedule(customUserDetails.getUsername(), requestDTO);
        return ResponseEntity.ok().build();
    }

}
