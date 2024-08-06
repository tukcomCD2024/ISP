package com.isp.backend.domain.flight.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FlightLikeRequest {

    private String carrierCode;

    private int totalPrice;

    private String abroadDuration;

    private String abroadDepartureTime;

    private String abroadArrivalTime;

    private String homeDuration;

    private String homeDepartureTime;

    private String homeArrivalTime;

    private String departureIataCode;

    private String arrivalIataCode;

    private String transferCount;

    private int adult ;

    private int children ;

}