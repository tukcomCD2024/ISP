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
public class DailySchedule {

    private String date; // 일정 날짜

    private List<ScheduleDetail> schedules; // 해당 날짜의 일정 목록

}
