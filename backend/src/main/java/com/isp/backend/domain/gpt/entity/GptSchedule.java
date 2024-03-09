package com.isp.backend.domain.gpt.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GptSchedule {
    private String date;
    private List<String> scheduleDetail;

    public GptSchedule(String date, List<String> scheduleDetail) {
        this.date = date;
        this.scheduleDetail = scheduleDetail;
    }
}
