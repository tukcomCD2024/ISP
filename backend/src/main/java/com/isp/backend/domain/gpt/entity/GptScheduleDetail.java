package com.isp.backend.domain.gpt.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GptScheduleDetail {
    private String detail;
    private Coordinate coordinate;

    public GptScheduleDetail(String detail, Coordinate coordinate) {
        this.detail = detail;
        this.coordinate = coordinate;
    }
}