package com.isp.backend.domain.country.controller;

import com.isp.backend.domain.country.dto.LocationRequestDTO;
import com.isp.backend.domain.country.dto.LocationResponseDTO;
import com.isp.backend.domain.country.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {

    @Autowired
    private CountryService countryService;

    /** 여행지 좌표 찾기 **/
    @PostMapping("/location")
    public ResponseEntity<LocationResponseDTO> findLocation(@RequestBody LocationRequestDTO requestDTO) {
        String country = requestDTO.getCountry();
        LocationResponseDTO responseDTO = countryService.findLocationByCity(country);
        if (responseDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }


}