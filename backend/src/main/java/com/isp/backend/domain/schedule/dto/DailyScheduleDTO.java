package com.isp.backend.domain.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyScheduleDTO {

    private String date; // 일정 날짜

    private List<ScheduleDetailDTO> schedules; // 해당 날짜의 일정 목록

}
