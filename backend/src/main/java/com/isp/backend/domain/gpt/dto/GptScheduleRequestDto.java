package com.isp.backend.domain.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GptScheduleRequestDto {
    private String destination;
    private String purpose;
    private String departureDate;
    private String returnDate;
}
