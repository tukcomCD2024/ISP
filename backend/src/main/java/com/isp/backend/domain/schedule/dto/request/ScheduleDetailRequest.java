package com.isp.backend.domain.schedule.dto.request;


import com.isp.backend.domain.scheduleDetail.entity.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDetailRequest {

    private String todo;        // 할 일

    private String place;       // 장소

    private ScheduleType type;        // 일정 유형

    private double budget;      // 예산

    private double latitude;    // 위도

    private double longitude;   // 경도

}
