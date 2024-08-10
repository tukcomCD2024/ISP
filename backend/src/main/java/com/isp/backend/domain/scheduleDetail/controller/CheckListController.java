package com.isp.backend.domain.scheduleDetail.controller;

import com.isp.backend.domain.scheduleDetail.dto.request.CheckListRequest;
import com.isp.backend.domain.scheduleDetail.dto.response.CheckListResponse;
import com.isp.backend.domain.scheduleDetail.service.CheckListService;
import com.isp.backend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.* ;

import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class CheckListController {

    private final CheckListService checkListService;

    /** 일정 체크리스트 저장 API **/
    @PostMapping("/{scheduleId}/checklists")
    public ResponseEntity<List<CheckListResponse>> addCheckLists(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                 @PathVariable Long scheduleId, @RequestBody List<CheckListRequest> checkListRequests) {
        String memberUid = customUserDetails.getUsername();
        List<CheckListResponse> checkListResponses = checkListService.addCheckLists(memberUid, scheduleId, checkListRequests);
        return ResponseEntity.ok(checkListResponses);
    }


    /** 여행 체크리스트 조회 API **/
    @GetMapping("/{scheduleId}/checklists")
    public ResponseEntity<List<CheckListResponse>> getCheckLists(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                 @PathVariable Long scheduleId) {
        String memberUid = customUserDetails.getUsername();
        List<CheckListResponse> checkListResponses = checkListService.getCheckLists(memberUid, scheduleId);
        return ResponseEntity.ok(checkListResponses);
    }


    /** 여행 체크리스트 삭제 API **/
    @DeleteMapping("/{scheduleId}/checklists/{checkListId}")
    public ResponseEntity<List<CheckListResponse>> deleteCheckList(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                   @PathVariable Long scheduleId,
                                                                   @PathVariable Long checkListId) {
        String memberUid = customUserDetails.getUsername();
        List<CheckListResponse> checkListResponses = checkListService.deleteCheckList(memberUid, scheduleId, checkListId);
        return ResponseEntity.ok(checkListResponses);
    }


    /** 여행 체크리스트 수정 API **/
    @PutMapping("/{scheduleId}/checklists")
    public ResponseEntity<List<CheckListResponse>> updateCheckLists(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                    @PathVariable Long scheduleId,
                                                                    @RequestBody List<CheckListResponse> checkListResponses) {
        String memberUid = customUserDetails.getUsername();
        List<CheckListResponse> updatedCheckListResponses = checkListService.updateCheckLists(memberUid, scheduleId, checkListResponses);
        return ResponseEntity.ok(updatedCheckListResponses);
    }


}
