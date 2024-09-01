package com.isp.backend.domain.scheduleImage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReadScheduleImageResponse {
    private Long scheduleId;
    private List<ScheduleImageResponse> scheduleImages;
}