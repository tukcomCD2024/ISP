package com.isp.backend.domain.schedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleListResponse {

    private Long id;

    private String scheduleName;

    private String startDate;

    private String endDate;

    private double totalPrice;

    private String imageUrl;

    private String country;

    private double latitude ;

    private double longitude ;

}
