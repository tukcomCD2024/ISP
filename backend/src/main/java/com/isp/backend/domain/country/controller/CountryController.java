package com.isp.backend.domain.country.controller;

import com.isp.backend.domain.country.dto.LocationResponseDTO;
import com.isp.backend.domain.country.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class CountryController {

    @Autowired
    private CountryService countryService;

    /** 여행지 좌표 찾기 **/
    @GetMapping("/location/{country}")
    public ResponseEntity<LocationResponseDTO> findLocation(@PathVariable String country) {
        LocationResponseDTO responseDTO = countryService.findLocationByCity(country);
        if (responseDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }



}