package com.isp.backend.domain.hotel.service;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Hotel;
import com.isp.backend.domain.hotel.dto.request.SearchGeocodeRequest;

public interface HotelService {
    public String searchHotelsByGeocode(SearchGeocodeRequest request) throws ResponseException;

}
