package com.isp.backend.domain.scheduleImage.dto.response;

import com.isp.backend.domain.scheduleImage.entity.ScheduleImage;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SaveScheduleImageResponse {
    private Long id;
    private Long scheduleId;
    private String path;
    private LocalDateTime saveDate;

    public SaveScheduleImageResponse(ScheduleImage scheduleImage) {
        this.id = scheduleImage.getId();
        this.scheduleId = scheduleImage.getSchedule().getId();
        this.path = scheduleImage.getPath();
        this.saveDate = scheduleImage.getSaveDate();
    }
}