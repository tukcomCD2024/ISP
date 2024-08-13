package com.isp.backend.domain.gpt.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GptScheduleDetail {
    private String detail;
    private Double price;
    private Coordinate coordinate;

    public GptScheduleDetail(String detail, Double price, Coordinate coordinate) {
        this.detail = detail;
        this.price = price;
        this.coordinate = coordinate;
    }
}