package com.isp.backend.domain.scheduleDetail.service;

import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.domain.schedule.entity.Schedule;
import com.isp.backend.domain.schedule.repository.ScheduleRepository;
import com.isp.backend.domain.schedule.service.ScheduleService;
import com.isp.backend.domain.scheduleDetail.dto.request.CheckListRequest;
import com.isp.backend.domain.scheduleDetail.dto.response.CheckListResponse;
import com.isp.backend.domain.scheduleDetail.entity.CheckList;
import com.isp.backend.domain.scheduleDetail.repository.CheckListRepository;
import com.isp.backend.global.exception.schedule.CheckListNotFoundException;
import com.isp.backend.global.exception.schedule.NotYourScheduleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class CheckListServiceImpl implements CheckListService {
    private final CheckListRepository checkListRepository;
    private final ScheduleService scheduleService;


    /** 여행 체크리스트 추가 API **/
    @Override
    @Transactional
    public List<CheckListResponse> addCheckLists(String uid, Long scheduleId, List<CheckListRequest> checkListRequests) {
        Member findMember = scheduleService.validateUserCheck(uid);
        Schedule findSchedule = scheduleService.validateSchedule(scheduleId);
        // 자신의 여행 일정인지 확인
        if (!findSchedule.getMember().equals(findMember)) {
            throw new NotYourScheduleException();
        }

        List<CheckListResponse> responses = new ArrayList<>();
        for (CheckListRequest request : checkListRequests) {
            CheckList checkList = CheckList.builder()
                    .todo(request.getTodo())
                    .isChecked(request.isCheck())
                    .schedule(findSchedule)
                    .build();
            checkListRepository.save(checkList);
            responses.add(new CheckListResponse(checkList));
        }

        return responses;
    }


    /** 여행 체크리스트 조회 API **/
    @Override
    @Transactional(readOnly = true)
    public List<CheckListResponse> getCheckLists(String uid, Long scheduleId) {
        Member findMember = scheduleService.validateUserCheck(uid);
        Schedule findSchedule = scheduleService.validateSchedule(scheduleId);
        // 자신의 여행 일정인지 확인
        if (!findSchedule.getMember().equals(findMember)) {
            throw new NotYourScheduleException();
        }

        List<CheckList> checkLists = checkListRepository.findBySchedule(findSchedule);
        List<CheckListResponse> responses = checkLists.stream()
                .map(CheckListResponse::new)
                .collect(Collectors.toList());

        return responses;
    }


    /** 여행 체크리스트 삭제 API **/
    @Override
    @Transactional
    public List<CheckListResponse> deleteCheckList(String uid, Long scheduleId, Long checkListId) {
        Member findMember = scheduleService.validateUserCheck(uid);
        Schedule findSchedule = scheduleService.validateSchedule(scheduleId);
        CheckList checkList = checkListRepository.findById(checkListId)
                .orElseThrow(CheckListNotFoundException::new);

        // 자신의 여행 일정인지 확인 + 체크리스트가 해당 스케줄에 속하는지 확인
        if (!findSchedule.getMember().equals(findMember) || !checkList.getSchedule().equals(findSchedule)) {
            throw new NotYourScheduleException();
        }

        checkListRepository.delete(checkList);

        List<CheckList> checkLists = checkListRepository.findBySchedule(findSchedule);
        List<CheckListResponse> responses = checkLists.stream()
                .map(CheckListResponse::new)
                .collect(Collectors.toList());

        return responses;
    }


    /** 여행 체크리스트 수정 API **/
    @Override
    @Transactional
    public List<CheckListResponse> updateCheckLists(String uid, Long scheduleId, List<CheckListResponse> checkListResponses) {
        Member findMember = scheduleService.validateUserCheck(uid);
        Schedule findSchedule = scheduleService.validateSchedule(scheduleId);

        if (!findSchedule.getMember().equals(findMember)) {
            throw new NotYourScheduleException();
        }

        for (CheckListResponse response : checkListResponses) {
            CheckList checkList = checkListRepository.findById(response.getId())
                    .orElseThrow(CheckListNotFoundException::new);
            if (!checkList.getSchedule().equals(findSchedule)) {
                throw new NotYourScheduleException();
            }
            checkList.setTodo(response.getTodo());
            checkList.setChecked(response.isCheck());
        }

        List<CheckList> updatedCheckLists = checkListRepository.findBySchedule(findSchedule);
        List<CheckListResponse> responses = updatedCheckLists.stream()
                .map(CheckListResponse::new)
                .collect(Collectors.toList());

        return responses;
    }

}
