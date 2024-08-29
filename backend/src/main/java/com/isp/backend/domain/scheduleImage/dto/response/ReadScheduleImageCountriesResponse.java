package com.isp.backend.domain.scheduleImage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReadScheduleImageCountriesResponse {
    private List<ScheduleLocationResponse> locationSchedules;
}