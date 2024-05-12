package com.isp.backend.domain.country.service;


import com.isp.backend.domain.country.dto.response.LocationResponse;
import com.isp.backend.domain.country.entity.Country;
import com.isp.backend.domain.country.repository.CountryRepository;
import com.isp.backend.global.exception.schedule.CountryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;


    /** 여행지 좌표 찾기 **/
    public Optional<LocationResponse> findLocationByCity(String city) {
        Optional<Country> findCountry = countryRepository.findIdByCity(city);
        return findCountry.map(country -> {
            LocationResponse locationDTO = new LocationResponse();
            locationDTO.setLatitude(country.getLatitude());
            locationDTO.setLongitude(country.getLongitude());
            return locationDTO;
        });
    }


}


