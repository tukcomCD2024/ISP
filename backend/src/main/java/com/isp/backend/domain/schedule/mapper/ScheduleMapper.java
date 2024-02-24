package com.isp.backend.domain.schedule.mapper;

import com.isp.backend.domain.country.entity.Country;
import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.domain.scheduleDetail.entity.ScheduleDetail;
import com.isp.backend.domain.schedule.dto.DailyScheduleDTO;
import com.isp.backend.domain.schedule.dto.ScheduleDetailDTO;
import com.isp.backend.domain.schedule.dto.ScheduleListResponseDTO;
import com.isp.backend.domain.schedule.dto.ScheduleSaveRequestDTO;
import com.isp.backend.domain.schedule.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScheduleMapper {

    // 여행 일정 요청 DTO -> 엔티티로 변환
    public Schedule toSchedulesEntity(ScheduleSaveRequestDTO scheduleSaveRequestDTO, Member member, Country country) {
        // 여행 일정 엔티티 생성
        Schedule schedule = Schedule.builder()
                .scheduleName(scheduleSaveRequestDTO.getScheduleName())
                .country(country)
                .startDate(scheduleSaveRequestDTO.getStartDate())
                .endDate(scheduleSaveRequestDTO.getEndDate())
                .member(member)
                .build();

        // 여행 일정 세부 항목 리스트 생성
        List<ScheduleDetail> scheduleDetails = scheduleSaveRequestDTO.getDailySchedules().stream()
                .flatMap(dailyScheduleDTO -> {
                    AtomicInteger num = new AtomicInteger(1); // 날짜별 일정 순서 카운터
                    return dailyScheduleDTO.getSchedules().stream()
                            .map(scheduleDetailDTO -> toScheduleDetailEntity(scheduleDetailDTO, dailyScheduleDTO, schedule, num.getAndIncrement()));
                })
                .collect(Collectors.toList());

        // 일정 세부를 일정 엔티티에 설정
        schedule.setScheduleDetails(scheduleDetails);

        return schedule;
    }

    // 일정 세부 DTO를 엔티티로 변환
    private ScheduleDetail toScheduleDetailEntity(ScheduleDetailDTO scheduleDetailDTO, DailyScheduleDTO dailyScheduleDTO, Schedule schedule, int num) {
        return ScheduleDetail.builder()
                .todo(scheduleDetailDTO.getTodo())
                .place(scheduleDetailDTO.getPlace())
                .budget(scheduleDetailDTO.getBudget())
                .latitude(scheduleDetailDTO.getLatitude())
                .longitude(scheduleDetailDTO.getLongitude())
                .date(dailyScheduleDTO.getDate())
                .num(num) // 일정 순서 저장
                .schedule(schedule)
                .build();
    }


    // 여행 전체 일정 목록 불러오기
    public ScheduleListResponseDTO toScheduleListResponseDTO(Schedule schedule) {
        return new ScheduleListResponseDTO(
                schedule.getId(),
                schedule.getScheduleName(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                schedule.getTotalPrice(),
                schedule.getCountry().getImageUrl()
        );
    }




}


