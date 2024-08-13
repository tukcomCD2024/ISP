package com.isp.backend.domain.gpt.entity;

import com.isp.backend.domain.gpt.constant.ParsingConstants;
import com.isp.backend.global.exception.gpt.NonValidatedParsingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class GptScheduleParser {

    private static final Pattern LINE_PATTERN = Pattern.compile("-\\s*(.*?):\\s*([\\d.]+,\\s*[\\d.]+)?\\s*\\((.*?)\\)");

    public List<GptSchedule> parseScheduleText(String scheduleText) {
        System.out.println(scheduleText);
        List<GptSchedule> schedules = new ArrayList<>();
        String[] entries = scheduleText.split(ParsingConstants.ENTRY_SEPARATOR.getStringValue());

        for (String entry : entries) {
            if (entry.trim().isEmpty()) continue;

            String[] lines = entry.split(ParsingConstants.LINE_SEPARATOR.getStringValue());
            String date = parseDate(lines[0]);

            List<GptScheduleDetail> scheduleDetails = new ArrayList<>();

            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                if (line.isEmpty()) continue;

                Matcher matcher = LINE_PATTERN.matcher(line);
                if (matcher.find()) {
                    String detail = matcher.group(1);
                    String coordinatesStr = matcher.group(2);
                    String priceStr = matcher.group(3);

                    Coordinate coordinate = parseCoordinates(coordinatesStr);
                    double price = parsePriceString(priceStr);

                    scheduleDetails.add(new GptScheduleDetail(detail, price, coordinate));
                } else {
                    System.out.println("767867889");
                    throw new NonValidatedParsingException();
                }
            }

            schedules.add(new GptSchedule(date, scheduleDetails));
        }

        return schedules;
    }

    private String parseDate(String line) {
        return line.trim().replace(ParsingConstants.DATE_SUFFIX.getStringValue(), "");
    }

    private Coordinate parseCoordinates(String coordinatesStr) {
        if (coordinatesStr == null || coordinatesStr.isEmpty()) {
            return new Coordinate(ParsingConstants.DEFAULT_COORDINATE.getDoubleValue(), ParsingConstants.DEFAULT_COORDINATE.getDoubleValue());
        }

        String[] parts = coordinatesStr.split(",");
        if (parts.length != 2) {
            System.out.println("1546351");
            throw new NonValidatedParsingException();
        }

        try {
            double latitude = Double.parseDouble(parts[0].trim());
            double longitude = Double.parseDouble(parts[1].trim());
            return new Coordinate(latitude, longitude);
        } catch (NumberFormatException e) {
            System.out.println("1234");
            throw new NonValidatedParsingException();
        }
    }

    private double parsePriceString(String priceStr) {
        if (ParsingConstants.PRICE_FREE.getStringValue().equals(priceStr)) {
            return 0.0;
        }

        try {
            return Double.parseDouble(priceStr.replaceAll("[^\\d.]", ""));
        } catch (NumberFormatException e) {
            System.out.println("5678");
            return ParsingConstants.DEFAULT_PRICE.getDoubleValue();
        }
    }
}