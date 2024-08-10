package com.isp.backend.domain.hotel.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Hotel;
import com.amadeus.shopping.HotelOffersSearch;
import com.google.gson.Gson;
import com.isp.backend.domain.hotel.dto.request.SearchGeocodeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class HotelServiceImpl implements HotelService {
    private final Amadeus amadeus;

    /**
     * 좌표 주변 호텔 리스트 API
     **/
    @Override
    public String searchHotelsByGeocode(SearchGeocodeRequest request) throws ResponseException {
        String latitude = request.getLatitude();
        String longitude = request.getLongitude();
        int radius = request.getRadius();
        String ratings = request.getRatings();

        Hotel[] hotelSearch =  amadeus.referenceData.locations.hotels.byGeocode.get(
                    Params.with("latitude", latitude)
                            .and("longitude", longitude)
                            .and("radius", radius)
                            .and("radiusUnit", "KM")
                            .and("ratings", ratings)
                            .and("hotelSource", "ALL")
            );
        Gson gson = new Gson();
        String hotelSearchJson = gson.toJson(hotelSearch);
        return hotelSearchJson;
        }


}
