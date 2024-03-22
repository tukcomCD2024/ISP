package com.isp.backend.domain.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleSaveRequest {

    private String scheduleName;   // 여행 일정 이름

    private String country;        // 여행할 국가

    private String startDate;      // 여행 시작 날짜

    private String endDate;        // 여행 종료 날짜

    private List<DailySchedule> dailySchedules;   // 하루 일정 목록

}
