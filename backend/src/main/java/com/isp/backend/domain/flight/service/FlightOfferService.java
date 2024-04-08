package com.isp.backend.domain.flight.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.google.gson.Gson;
import com.isp.backend.domain.country.entity.Country;
import com.isp.backend.domain.country.repository.CountryRepository;
import com.isp.backend.domain.flight.dto.request.FlightSearchRequest;
import com.isp.backend.domain.flight.mapper.FlightOfferProcessor;
import com.isp.backend.global.exception.schedule.CountryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FlightOfferService {

    private final Amadeus amadeus;
    private final CountryRepository countryRepository;
    private final FlightOfferProcessor flightOfferProcessor ;


    /** 항공편 조회 **/
    public String getFlightOffers(FlightSearchRequest request) throws ResponseException {

        String originLocationCode = findAirportCode(request.getOriginCity());
        String destinationLocationCode = findAirportCode(request.getDestinationCity());

        FlightOfferSearch[] flightOffers = amadeus.shopping.flightOffersSearch.get(
                Params.with("originLocationCode", originLocationCode)
                        .and("destinationLocationCode", destinationLocationCode)
                        .and("departureDate", request.getDepartureDate())
                        .and("returnDate", request.getReturnDate())
                        .and("adults", request.getAdults())
                        .and("children", request.getChildren())
                        .and("max", request.getMax())
                        .and("nonStop", request.isNonStop())
                        .and("currencyCode","KRW")  // 원화 설정 - 추후 유저에게 입력받을 수 있게 변경
        );

        // FlightOfferSearch 배열을 JSON 문자열로 변환
        Gson gson = new Gson();
        String flightOffersJson = gson.toJson(flightOffers);
        return flightOfferProcessor.processFlightOffers(flightOffersJson);   // 원하는 정보만 조회
    }


    /** 공항 코드 찾기 **/
    private String findAirportCode(String countryName) {
        Country findCountry = countryRepository.findAirportCodeByCity(countryName)
                .orElseThrow(()-> new CountryNotFoundException());
        return findCountry.getAirportCode();
    }

}
