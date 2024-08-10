package com.isp.backend.domain.scheduleImage.dto;

import com.isp.backend.domain.scheduleImage.entity.ScheduleImage;
import lombok.Getter;

@Getter
public class SaveScheduleImageResponse {
    private Long id;
    private Long scheduleId;
    private String path;

    public SaveScheduleImageResponse(ScheduleImage scheduleImage) {
        this.id = scheduleImage.getId();
        this.scheduleId = scheduleImage.getSchedule().getId();
        this.path = scheduleImage.getPath();
    }
}