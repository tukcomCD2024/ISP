package com.isp.backend.domain.gpt.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GptSchedule {
    private List<GptScheduleDetail> gptScheduleDetails;

    @Builder
    public GptSchedule(List<GptScheduleDetail> gptScheduleDetails) {
        this.gptScheduleDetails = gptScheduleDetails;
    }
}
