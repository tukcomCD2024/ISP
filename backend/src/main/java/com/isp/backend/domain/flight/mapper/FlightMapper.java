package com.isp.backend.domain.flight.mapper;

import com.isp.backend.domain.country.entity.Country;
import com.isp.backend.domain.flight.dto.request.FlightLikeRequest;
import com.isp.backend.domain.flight.dto.response.FlightLikeResponse;
import com.isp.backend.domain.flight.entity.Flight;
import com.isp.backend.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FlightMapper {

    public Flight toEntity(FlightLikeRequest request, Member member, Country departureIataCode, Country arrivalIataCode) {
        return Flight.builder()
                .member(member) // 주입된 member 객체 사용
                .carrierCode(request.getCarrierCode())
                .price((double) request.getTotalPrice())
                .abroadDuration(request.getAbroadDuration())
                .abroadDepartureTime(request.getAbroadDepartureTime())
                .abroadArrivalTime(request.getAbroadArrivalTime())
                .homeDuration(request.getHomeDuration())
                .homeDepartureTime(request.getHomeDepartureTime())
                .homeArrivalTime(request.getHomeArrivalTime())
                .departureIataCode(departureIataCode)
                .arrivalIataCode(arrivalIataCode)
                .transferCount(Integer.parseInt(request.getTransferCount()))
                .build();
    }

    public FlightLikeResponse toFlightLikeRequest(Flight flight) {
        FlightLikeResponse response = new FlightLikeResponse();
        response.setId(flight.getId());
        response.setCarrierCode(flight.getCarrierCode());
        response.setTotalPrice((int) Math.round(flight.getPrice()));
        response.setAbroadDuration(flight.getAbroadDuration());
        response.setAbroadDepartureTime(flight.getAbroadDepartureTime());
        response.setAbroadArrivalTime(flight.getAbroadArrivalTime());
        response.setHomeDuration(flight.getHomeDuration());
        response.setHomeDepartureTime(flight.getHomeDepartureTime());
        response.setHomeArrivalTime(flight.getHomeArrivalTime());
        response.setDepartureIataCode(flight.getDepartureIataCode().getAirportCode());
        response.setArrivalIataCode(flight.getArrivalIataCode().getAirportCode());
        response.setTransferCount(String.valueOf(flight.getTransferCount()));
        return response;
    }

    public List<FlightLikeResponse> toFlightLikeRequestList(List<Flight> flights) {
        return flights.stream()
                .map(this::toFlightLikeRequest)
                .collect(Collectors.toList());
    }

}