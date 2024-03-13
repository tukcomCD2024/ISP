package com.isp.backend.domain.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GptScheduleRequestDto {
    private String destination;
    private List<String> purpose;
    private String departureDate;
    private String returnDate;
}
