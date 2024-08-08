package com.isp.backend.domain.scheduleImage.dto;

import com.isp.backend.domain.scheduleImage.entity.ScheduleImage;
import lombok.Getter;

@Getter
public final class SaveScheduleImageResponse {
    private final ScheduleImage scheduleImage;

    public SaveScheduleImageResponse(ScheduleImage scheduleImage) {
        this.scheduleImage = scheduleImage;
    }
}