package com.isp.backend.domain.schedule.controller;

import com.isp.backend.domain.schedule.dto.response.FastestScheduleResponse;
import com.isp.backend.domain.schedule.dto.response.ScheduleListResponse;
import com.isp.backend.domain.schedule.dto.request.ScheduleSaveRequest;
import com.isp.backend.domain.schedule.service.ScheduleService;
import com.isp.backend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /** 여행 일정 저장 API **/
    @PostMapping()
    public ResponseEntity<Void> saveSchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                             @RequestBody ScheduleSaveRequest requestDTO) {
        scheduleService.saveSchedule(customUserDetails.getUsername(), requestDTO);
        return ResponseEntity.ok().build();
    }


    /** 여행 일정 목록 조회 API **/
    @GetMapping()
    public ResponseEntity<List<ScheduleListResponse>> getScheduleList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String memberUid = userDetails.getUsername();
        List<ScheduleListResponse> scheduleList = scheduleService.getScheduleList(memberUid);
        return ResponseEntity.ok(scheduleList);
    }


    /** 여행 일정 상세 조회 API **/
    @GetMapping("/details/{scheduleId}")
    public ResponseEntity<ScheduleSaveRequest> getScheduleDetail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @PathVariable Long scheduleId) {
        String memberUid = userDetails.getUsername();
        ScheduleSaveRequest scheduleDetail = scheduleService.getScheduleDetail(memberUid, scheduleId);
        return ResponseEntity.ok(scheduleDetail);
    }


    /** 여행 일정 수정 API **/
    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleSaveRequest> updateSchedule(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                              @PathVariable Long scheduleId,
                                                              @RequestBody ScheduleSaveRequest requestDTO) {
        ScheduleSaveRequest updatedSchedule = scheduleService.updateSchedule(userDetails.getUsername(), scheduleId, requestDTO);
        return ResponseEntity.ok(updatedSchedule);
    }


    /** 여행 일정 삭제 API **/
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @PathVariable Long scheduleId) {
        String memberUid = userDetails.getUsername();
        scheduleService.deleteSchedule(memberUid, scheduleId);
        return ResponseEntity.ok().build();
    }


    /** 나의 가까운 일정 조회 API **/
    @GetMapping("/dday")
    public ResponseEntity<FastestScheduleResponse> getFastestSchedule(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String memberUid = userDetails.getUsername();
        FastestScheduleResponse fastestScheduleResponse = scheduleService.getFastestSchedule(memberUid);
        return ResponseEntity.ok(fastestScheduleResponse);
    }


    /** 내가 최근 생성한 5개 일정 조회 API **/
    public ResponseEntity<>


}