package com.isp.backend.domain.schedule.controller;

import com.isp.backend.domain.schedule.dto.ScheduleListResponseDTO;
import com.isp.backend.domain.schedule.dto.ScheduleRequestDTO;
import com.isp.backend.domain.schedule.dto.ScheduleResponseDTO;
import com.isp.backend.domain.schedule.dto.ScheduleSaveRequestDTO;
import com.isp.backend.domain.schedule.entity.Schedule;
import com.isp.backend.domain.schedule.service.ScheduleService;
import com.isp.backend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
     * 여행 일정 생성형 API
     */
    @PostMapping("/")
    public ResponseEntity<ScheduleResponseDTO> createSchedule(@RequestBody ScheduleRequestDTO scheduleRequestDTO) {

        ScheduleResponseDTO scheduleResponseDTO = scheduleService.createSchedule(scheduleRequestDTO);
        return ResponseEntity.ok(scheduleResponseDTO);
    }


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

    /**
     * 여행 일정 상세 조회 API
     */
    @GetMapping("/detail/{scheduleId}")
    public ResponseEntity<ScheduleSaveRequestDTO> getScheduleDetail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                    @PathVariable Long scheduleId) {
        String uid = userDetails.getUsername();
        ScheduleSaveRequestDTO scheduleDetail = scheduleService.getScheduleDetail(uid, scheduleId);
        return ResponseEntity.ok(scheduleDetail);
    }

    /**
     * 여행 일정 삭제 API
     */
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @PathVariable Long scheduleId) {
        String uid = userDetails.getUsername();
        scheduleService.deleteSchedule(uid, scheduleId);
        return ResponseEntity.ok().build();
    }


    /**
     * 여행 일정 수정 API
     */
    @PutMapping("/update/{scheduleId}")
    public ResponseEntity<ScheduleSaveRequestDTO> updateSchedule(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @PathVariable Long scheduleId,
                                               @RequestBody ScheduleSaveRequestDTO requestDTO) {
        ScheduleSaveRequestDTO updatedSchedule = scheduleService.updateSchedule(userDetails.getUsername(), scheduleId, requestDTO);
//        return ResponseEntity.ok().build();  == public void로
        return ResponseEntity.ok(updatedSchedule);
    }


}