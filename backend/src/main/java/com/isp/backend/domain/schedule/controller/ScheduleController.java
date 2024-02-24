package com.isp.backend.domain.schedule.controller;

import com.isp.backend.domain.schedule.dto.ScheduleListResponseDTO;
import com.isp.backend.domain.schedule.dto.ScheduleSaveRequestDTO;
import com.isp.backend.domain.schedule.service.ScheduleService;
import com.isp.backend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * 여행 일정 저장 API
     */
    @PostMapping("/save")
    public ResponseEntity<Void> saveSchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                             @RequestBody ScheduleSaveRequestDTO requestDTO) {
        scheduleService.saveSchedule(customUserDetails.getUsername(), requestDTO);
        return ResponseEntity.ok().build();
    }


    /**
     * 여행 일정 목록 조회 API
     */
    @GetMapping("/list")
    public ResponseEntity<List<ScheduleListResponseDTO>> getScheduleList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String uid = userDetails.getUsername();
        List<ScheduleListResponseDTO> scheduleList = scheduleService.getScheduleList(uid);
        return ResponseEntity.ok(scheduleList);
    }
    @PostMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleResponseDTO login(@RequestBody ScheduleRequestDTO scheduleRequestDTO) {
        return new ScheduleResponseDTO(schedule);
        Schedule schedule = scheduleService.create(scheduleRequestDTO.toEntity());
    }
}