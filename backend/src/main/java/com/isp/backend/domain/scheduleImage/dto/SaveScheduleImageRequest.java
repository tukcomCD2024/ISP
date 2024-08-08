package com.isp.backend.domain.scheduleImage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SaveScheduleImageRequest {
    private Long scheduleId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime saveDate;

    public Long scheduleId() {
        return scheduleId;
    }
}