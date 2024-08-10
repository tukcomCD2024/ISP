package com.isp.backend.domain.flight.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FlightSearchRequest {

    private String originCity;

    private String destinationCity;

    private String departureDate;

    private String returnDate;

    private int adults;

    private int children;

    private int max ;

    private boolean nonStop ; // 직항 항공편 유무

}