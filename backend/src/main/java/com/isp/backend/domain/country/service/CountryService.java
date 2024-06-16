package com.isp.backend.domain.country.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isp.backend.domain.country.dto.response.DailyWeatherResponse;
import com.isp.backend.domain.country.dto.response.LocationResponse;
import com.isp.backend.domain.country.dto.response.WeatherResponse;
import com.isp.backend.domain.country.entity.Country;
import com.isp.backend.domain.country.mapper.WeatherMapper;
import com.isp.backend.domain.country.repository.CountryRepository;
import com.isp.backend.global.exception.openApi.OpenWeatherSearchFailedException;
import com.isp.backend.global.exception.schedule.CountryNotFoundException;
import com.isp.backend.global.s3.constant.S3BucketDirectory;
import com.isp.backend.global.s3.service.S3ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;


@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Value("${api-key.open-weather}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private S3ImageService s3ImageService;


    /** 여행지 좌표 찾기 **/
    public Optional<LocationResponse> findLocationByCity(String city) {
        Optional<Country> findCountry = countryRepository.findIdByCity(city);

        if (findCountry.isPresent()) {
            return findCountry.map(country -> {
                LocationResponse locationDTO = new LocationResponse();
                locationDTO.setLatitude(country.getLatitude());
                locationDTO.setLongitude(country.getLongitude());
                return locationDTO;
            });
        } else {
            throw new CountryNotFoundException();
        }
    }


    /** 여행지의 날씨 정보 가져오기 **/
    public WeatherResponse getCurrentWeather(String city) {
        Optional<Country> findCountry = findCountryByCity(city);
        Country country = findCountry.get();
        double latitude = country.getLatitude();
        double longitude = country.getLongitude();

        String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s", latitude, longitude, apiKey);
        String jsonResponse = restTemplate.getForObject(url, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        WeatherResponse weatherResponse = new WeatherResponse();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            JsonNode weatherNode = rootNode.path("weather").get(0);
            weatherResponse.setMain(weatherNode.path("main").asText());
            String descriptionId = weatherNode.path("id").asText(); // ID 값 가져오기
            String descriptionTranslation = WeatherMapper.getWeatherDescriptionTranslation(descriptionId); // 날씨 설명
            weatherResponse.setDescription(descriptionTranslation);

            String icon = weatherNode.path("icon").asText();
            weatherResponse.setIconUrl(s3ImageService.get(S3BucketDirectory.WEATHER.getDirectory(), icon + ".png"));

            JsonNode mainNode = rootNode.path("main");
            weatherResponse.setTemp(convertToCelsius(mainNode.path("temp").asDouble()));
            weatherResponse.setTempMin(convertToCelsius(mainNode.path("temp_min").asDouble()));
            weatherResponse.setTempMax(convertToCelsius(mainNode.path("temp_max").asDouble()));

            int timezoneOffset = rootNode.path("timezone").asInt();
            weatherResponse.setLocalTime(getLocalTime(timezoneOffset));

        } catch (IOException e) {
            e.printStackTrace();
            throw new OpenWeatherSearchFailedException();
        }

        return weatherResponse;
    }


    /** 한주의 날씨 정보 조회 **/
    public List<DailyWeatherResponse> getWeeklyWeather(String city, LocalTime requestedTime) {
        Optional<Country> findCountry = findCountryByCity(city);
        Country country = findCountry.get();
        double latitude = country.getLatitude();
        double longitude = country.getLongitude();

        List<DailyWeatherResponse> weeklyWeather = new ArrayList<>();
        String url = String.format("http://api.openweathermap.org/data/2.5/forecast?lat=%f&lon=%f&appid=%s", latitude, longitude, apiKey);
        String jsonResponse = restTemplate.getForObject(url, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode forecastList = rootNode.path("list");

            Map<String, List<Double>> dailyTemperatures = new HashMap<>(); // 각 날짜의 온도 목록을 저장할 맵

            for (JsonNode forecast : forecastList) {
                String dateTime = forecast.path("dt_txt").asText(); // 예측 시간 정보
                LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                // 요청된 시간대의 온도만 고려
                if (localDateTime.toLocalTime().equals(requestedTime)) {
                    String dateString = localDateTime.toLocalDate().toString();
                    double temperature = forecast.path("main").path("temp").asDouble(); // 온도 정보

                    // 해당 날짜의 온도 목록에 추가
                    dailyTemperatures.computeIfAbsent(dateString, k -> new ArrayList<>()).add(temperature);
                }
            }

            // 각 날짜의 평균 온도를 계산하여 DailyWeatherResponse 객체로 변환한 후 리스트에 추가
            dailyTemperatures.forEach((dateString, temperatures) -> {
                double avgTemperature = temperatures.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                String temperature = convertToCelsius(avgTemperature);

                // 아이콘 URL 가져오기
                String iconCode = forecastList.get(0).path("weather").get(0).path("icon").asText();
                String iconUrl = String.format(s3ImageService.get(S3BucketDirectory.WEATHER.getDirectory(), iconCode + ".png"));

                DailyWeatherResponse dailyWeather = new DailyWeatherResponse();
                dailyWeather.setDate(parseDayOfWeek(dateString));
                dailyWeather.setTemp(temperature);
                dailyWeather.setIconUrl(iconUrl);

                weeklyWeather.add(dailyWeather);
            });

            // 날짜 순서대로 정렬
            weeklyWeather.sort(Comparator.comparing(DailyWeatherResponse::getDate));

        } catch (IOException e) {
            e.printStackTrace();
            throw new OpenWeatherSearchFailedException();
        }

        return weeklyWeather;
    }


    /** 현지 시간으로 변환 **/
    private String getLocalTime(int timezoneOffset) {
        Instant now = Instant.now();
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(timezoneOffset);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(now, offset);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return localDateTime.format(formatter);
    }


    /** 온도를 섭씨로 변환 **/
    private String convertToCelsius(double kelvin) {
        double celsius = kelvin - 273.15;
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(celsius);
    }


    /** 날짜 문자열에서 요일을 파싱 **/
    private String parseDayOfWeek(String dateString) {
        LocalDate localDate = LocalDate.parse(dateString);
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        String weekDay = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN);
        return (dateString + "," +  weekDay);
    }



    /** 도시이름으로 나라 찾기 **/
    public Optional<Country> findCountryByCity(String city) {
        Optional<Country> findCountry = countryRepository.findIdByCity(city);
        if (!findCountry.isPresent()) {
            throw new CountryNotFoundException();
        }
        return findCountry;
    }


}
