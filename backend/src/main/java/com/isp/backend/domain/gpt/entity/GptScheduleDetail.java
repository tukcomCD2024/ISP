package com.isp.backend.domain.gpt.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class GptScheduleDetail {
    private LocalDateTime day;
    private List<String> details;

    @Builder
    public GptScheduleDetail(LocalDateTime day, List<String> details) {
        this.day = day;
        this.details = details;
    }
}
