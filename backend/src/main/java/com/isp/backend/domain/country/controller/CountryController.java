package com.isp.backend.domain.country.controller;

import com.isp.backend.domain.country.dto.request.LocationRequest;
import com.isp.backend.domain.country.dto.response.DailyWeatherResponse;
import com.isp.backend.domain.country.dto.response.ExchangeRateResponse;
import com.isp.backend.domain.country.dto.response.LocationResponse;
import com.isp.backend.domain.country.dto.response.WeatherResponse;
import com.isp.backend.domain.country.entity.ExchangeRate;
import com.isp.backend.domain.country.service.CountryService;
import com.isp.backend.domain.country.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;
    private final ExchangeRateService exchangeRateService ;

    /** 여행지 좌표 찾기 **/
    @PostMapping("/locations")
    public ResponseEntity<LocationResponse> findLocation(@RequestBody LocationRequest requestDTO) {
        String country = requestDTO.getCountry();
        Optional<LocationResponse> responseDTO = countryService.findLocationByCity(country);
        if (responseDTO.isPresent()) {
            return ResponseEntity.ok(responseDTO.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    /** 현재 날씨 정보 가져오기 **/
    @PostMapping("/weather/current")
    public ResponseEntity<WeatherResponse> getCurrentWeather(@RequestBody LocationRequest requestDTO) {
        String city = requestDTO.getCountry();
        WeatherResponse weatherResponse = countryService.getCurrentWeather(city);
        return ResponseEntity.ok(weatherResponse);
    }


    /** 한 주 날씨 정보 조회 **/
    @PostMapping("/weather/weekly")
    public ResponseEntity<List<DailyWeatherResponse>> getWeeklyWeather(@RequestBody LocationRequest requestDTO) {
        String city = requestDTO.getCountry();
        LocalTime requestedTime = LocalTime.of(12, 0);
        List<DailyWeatherResponse> weeklyWeather = countryService.getWeeklyWeather(city, requestedTime);
        return ResponseEntity.ok(weeklyWeather);
    }


    /** 환율 정보 업데이트 API **/
    @GetMapping("/exchange-rates/update")
    public String updateExchangeRate() {
        try {
            exchangeRateService.updateExchangeRates();
            return "updated Successfully";
        } catch (Exception e) {
            return "failed to update exchange rates: " + e.getMessage();
        }
    }

    /** 환율 정보 조회 API **/
    @GetMapping("/exchange-rates")
    public ResponseEntity<List<ExchangeRateResponse>> getAllExchangeRates() {
        List<ExchangeRateResponse> exchangeRates = exchangeRateService.getAllExchangeRates();
        return ResponseEntity.ok(exchangeRates);

    }

}