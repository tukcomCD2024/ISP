package com.isp.backend.domain.flight.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SkyScannerRequest {

    private String departureIataCode;

    private String arrivalIataCode;

    private String departureDate;

    private String returnDate;

    private int adult;

    private int children;

    private String departureTime;

    private int transferCount;

}