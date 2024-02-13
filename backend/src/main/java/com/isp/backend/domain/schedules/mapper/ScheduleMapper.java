package com.isp.backend.domain.schedules.mapper;

import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.domain.scheduleDetail.entity.ScheduleDetail;
import com.isp.backend.domain.schedules.dto.DailyScheduleDTO;
import com.isp.backend.domain.schedules.dto.ScheduleDetailDTO;
import com.isp.backend.domain.schedules.dto.SchedulesRequestDTO;
import com.isp.backend.domain.schedules.entity.Schedules;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScheduleMapper {

    public Schedules toSchedulesEntity(SchedulesRequestDTO schedulesRequestDTO, Member member) {
        Schedules schedules = Schedules.builder()
                .scheduleName(schedulesRequestDTO.getScheduleName())
                .startDate(schedulesRequestDTO.getStartDate())
                .endDate(schedulesRequestDTO.getEndDate())
                .member(member)
                .build();

        List<ScheduleDetail> scheduleDetails = schedulesRequestDTO.getDailySchedules().stream()
                .flatMap(dailyScheduleDTO -> dailyScheduleDTO.getSchedules().stream()
                        .map(scheduleDetailDTO -> toScheduleDetailEntity(scheduleDetailDTO, dailyScheduleDTO, schedules)))
                .collect(Collectors.toList());

        schedules.setScheduleDetails(scheduleDetails);

        return schedules;
    }

    private ScheduleDetail toScheduleDetailEntity(ScheduleDetailDTO scheduleDetailDTO, DailyScheduleDTO dailyScheduleDTO, Schedules schedules) {
        return ScheduleDetail.builder()
                .todo(scheduleDetailDTO.getTodo())
                .place(scheduleDetailDTO.getPlace())
                .budget(scheduleDetailDTO.getBudget())
                .latitude(scheduleDetailDTO.getLatitude())
                .longitude(scheduleDetailDTO.getLongitude())
                .date(dailyScheduleDTO.getDate())
                .schedules(schedules)
                .build();
    }
}


