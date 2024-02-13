package com.isp.backend.domain.schedules.service;

import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.domain.member.repository.MemberRepository;
import com.isp.backend.domain.scheduleDetail.entity.ScheduleDetail;
import com.isp.backend.domain.scheduleDetail.repository.ScheduleDetailRepository;
import com.isp.backend.domain.schedules.dto.DailyScheduleDTO;
import com.isp.backend.domain.schedules.dto.ScheduleDetailDTO;
import com.isp.backend.domain.schedules.dto.SchedulesRequestDTO;
import com.isp.backend.domain.schedules.entity.Schedules;
import com.isp.backend.domain.schedules.mapper.ScheduleMapper;
import com.isp.backend.domain.schedules.repository.SchedulesRepository;
import com.isp.backend.global.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final SchedulesRepository schedulesRepository ;
    private final ScheduleDetailRepository scheduleRepository ;
    private final MemberRepository memberRepository ;
    private final ScheduleMapper scheduleMapper;


    @Transactional
    public void saveSchedule(String uid, SchedulesRequestDTO schedulesRequestDTO) {
        // 유저 정보 확인
        Member member = memberRepository.findByUid(uid)
                .orElseThrow(MemberNotFoundException::new);

        // 여행 일정 저장
        Schedules schedules = scheduleMapper.toSchedulesEntity(schedulesRequestDTO, member);

        // 여행 순서 저장 + 총 경비 계산
//        getNumberAndSumBuget(schedules);

        schedulesRepository.save(schedules);
    }


//    @Transactional
//    public void getNumberAndSumBuget(Schedules schedules) {
//        List<ScheduleDetail> allDetails = schedules.getScheduleDetails();
//        double totalPrice = 0.0;
//        int num = 1;
//
//        for (DailyScheduleDTO dailySchedule : schedules.getScheduleDetails()) {
//            for (ScheduleDetailDTO detailDTO : dailySchedule.getSchedules()) {
//                ScheduleDetail detail = allDetails.stream()
//                        .filter(d -> d.getDate().equals(dailySchedule.getDate()))
//                        .filter(d -> d.getTodo().equals(detailDTO.getTodo()))
//                        .findFirst()
//                        .orElseThrow(() -> new IllegalArgumentException("Invalid detail: " + detailDTO));
//
//                detail.setNum(num++);
//                totalPrice += detailDTO.getBudget();
//            }
//        }
//
//        schedules.setTotalPrice(totalPrice);
//    }



}
