package com.isp.backend.domain.country.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/** weather id 값을 통해 description 출력 **/
public class WeatherMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String MAPPING_FILE_PATH = "/weather_mapping.json";

    public static String getWeatherDescriptionTranslation(String descriptionId) {
        try {
            InputStream inputStream = WeatherMapper.class.getResourceAsStream(MAPPING_FILE_PATH);
            JsonNode mappingNode = objectMapper.readTree(inputStream);
            JsonNode descriptionNode = mappingNode.get(descriptionId);

            // 만약 해당 ID에 대한 설명이 없다면 기본값 반환  --> 추후 공통 에러 반환
            if (descriptionNode == null) {
                return "해당 설명을 찾을 수 없습니다.";
            }
            return descriptionNode.asText();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read weather mapping file", e);
        }
    }
}
