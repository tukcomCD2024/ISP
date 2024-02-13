package com.isp.backend.domain.schedules.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDetailDTO {

    private String todo;        // 할 일

    private String place;       // 장소

    private double budget;      // 예산

    private double latitude;    // 위도

    private double longitude;   // 경도

}
