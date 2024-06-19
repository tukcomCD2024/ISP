package com.isp.backend.domain.gpt.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GptSchedule {
    private String date;
    private List<GptScheduleDetail> scheduleDetail;

    public GptSchedule(String date, List<GptScheduleDetail> scheduleDetail) {
        this.date = date;
        this.scheduleDetail = scheduleDetail;
    }
}