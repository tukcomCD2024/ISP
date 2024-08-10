package com.isp.backend.domain.scheduleDetail.service;

import com.isp.backend.domain.scheduleDetail.dto.request.CheckListRequest;
import com.isp.backend.domain.scheduleDetail.dto.response.CheckListResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CheckListService {
    @Transactional
    List<CheckListResponse> addCheckLists(String uid, Long scheduleId, List<CheckListRequest> checkListRequests);

    @Transactional(readOnly = true)
    List<CheckListResponse> getCheckLists(String uid, Long scheduleId);

    @Transactional
    List<CheckListResponse> deleteCheckList(String uid, Long scheduleId, Long checkListId);

    @Transactional
    List<CheckListResponse> updateCheckLists(String uid, Long scheduleId, List<CheckListResponse> checkListResponses);
}
