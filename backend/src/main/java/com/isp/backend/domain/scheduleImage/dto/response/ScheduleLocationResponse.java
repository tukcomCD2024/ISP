package com.isp.backend.domain.scheduleImage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ScheduleLocationResponse {
    private double latitude;
    private double longitude;
    private List<Long> scheduleIds;
}