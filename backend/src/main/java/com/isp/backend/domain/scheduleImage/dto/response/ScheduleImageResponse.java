package com.isp.backend.domain.scheduleImage.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleImageResponse {
    private Long scheduleImageId;
    private String path;
    private LocalDateTime saveDate;

    public ScheduleImageResponse(Long scheduleImageId, String path, LocalDateTime saveDate) {
        this.scheduleImageId = scheduleImageId;
        this.path = path;
        this.saveDate = saveDate;
    }
}