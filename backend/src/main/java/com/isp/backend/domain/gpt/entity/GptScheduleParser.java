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

    private static final Pattern LINE_PATTERN =Pattern.compile("-\\s*(.*?):\\s*([\\d.]+),\\s*([-\\d.]+)\\s*\\[(\\d+(\\.\\d+)?)\\]");


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
                    try {
                        String detail = matcher.group(1);
                        String latitude = matcher.group(2);
                        String longitude = matcher.group(3);
                        String priceString = matcher.group(4);

                        Coordinate coordinate = new Coordinate(Double.parseDouble(latitude), Double.parseDouble(longitude));
                        double price = parsePriceString(priceString);

                        scheduleDetails.add(new GptScheduleDetail(detail, price, coordinate));
                    } catch (Exception e) {
                        System.err.println("Error processing line: " + line);
                        e.printStackTrace();
                        throw new NonValidatedParsingException();
                    }
                } else {
                    System.out.println("No match found for line: " + line);
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


    private double parsePriceString(String priceStr) {
        if (ParsingConstants.PRICE_FREE.getStringValue().equals(priceStr)) {
            return 0.0;
        }

        try {
            return Double.parseDouble(priceStr.replaceAll("[^\\d.]", ""));
        } catch (NumberFormatException e) {
            return ParsingConstants.DEFAULT_PRICE.getDoubleValue();
        }
    }
}