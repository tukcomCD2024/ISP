package com.isp.backend.domain.gpt.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GptScheduleRequest {
    private String destination;
    private List<String> purpose;
    private String departureDate;
    private String returnDate;
}
