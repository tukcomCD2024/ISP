package com.isp.backend.domain.country.service;


import com.isp.backend.domain.country.dto.LocationResponseDTO;
import com.isp.backend.domain.country.entity.Country;
import com.isp.backend.domain.country.repository.CountryRepository;
import com.isp.backend.global.exception.schedule.CountryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;


    /** 여행지 좌표 찾기 **/
    public LocationResponseDTO findLocationByCity(String city) {
        Country country = countryRepository.findIdByCity(city);
        if (country == null) {
            throw new CountryNotFoundException();
        }
        LocationResponseDTO locationDTO = new LocationResponseDTO();
        locationDTO.setLatitude(country.getLatitude());
        locationDTO.setLongitude(country.getLongitude());
        return locationDTO;
    }


}


