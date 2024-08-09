package com.isp.backend.domain.country.service;

import com.isp.backend.domain.country.dto.response.DailyWeatherResponse;
import com.isp.backend.domain.country.dto.response.LocationResponse;
import com.isp.backend.domain.country.dto.response.WeatherResponse;
import com.isp.backend.domain.country.entity.Country;

import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface CountryService {
    Optional<LocationResponse> findLocationByCity(String city);

    WeatherResponse getCurrentWeather(String city);

    List<DailyWeatherResponse> getWeeklyWeather(String city, LocalTime requestedTime);

    Optional<Country> findCountryByCity(String city);


}
