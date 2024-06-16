package com.isp.backend.domain.flight.service;

import com.amadeus.exceptions.ResponseException;
import com.isp.backend.domain.flight.dto.request.FlightLikeRequest;
import com.isp.backend.domain.flight.dto.request.FlightSearchRequest;
import com.isp.backend.domain.flight.dto.request.SkyScannerRequest;
import com.isp.backend.domain.flight.dto.response.FlightLikeResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FlightOfferService {

    String getFlightOffers(FlightSearchRequest request) throws ResponseException;

    String generateSkyscannerUrl(SkyScannerRequest request);

    @Transactional
    Long addLikeFlight(String uid, FlightLikeRequest flightLikeRequest);

    List<FlightLikeResponse> getLikedFlights(String memberUid);

    @Transactional
    void deleteLikeFlight(String memberUid, Long id);
}
