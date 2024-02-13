package com.isp.backend.domain.schedules.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchedulesRequestDTO {

    private String scheduleName;   // 여행 일정 이름

    private String startDate;      // 여행 시작 날짜

    private String endDate;        // 여행 종료 날짜

    private List<DailyScheduleDTO> dailySchedules;   // 하루 일정 목록

}
