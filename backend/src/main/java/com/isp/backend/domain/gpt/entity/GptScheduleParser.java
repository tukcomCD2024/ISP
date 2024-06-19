package com.isp.backend.domain.gpt.entity;

import com.isp.backend.domain.gpt.constant.ParsingConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class GptScheduleParser {

    public List<GptSchedule> parseScheduleText(String scheduleText) {
        System.out.println(scheduleText);
        List<GptSchedule> schedules = new ArrayList<>();
        Pattern datePattern = Pattern.compile(ParsingConstants.DATE_REGEX);
        Pattern detailPattern = Pattern.compile("^-\\s*(.*?)(\\d+\\.\\d{1,8}),\\s*(\\d+\\.\\d{1,8})$");

        List<String> lines = List.of(scheduleText.split(ParsingConstants.NEW_LINE_REGEX));
        List<GptScheduleDetail> currentScheduleDetail = new ArrayList<>();
        String currentDate = "";

        for (String line : lines) {
            Matcher dateMatcher = datePattern.matcher(line);
            if (dateMatcher.find()) {
                if (!currentDate.isEmpty() && !currentScheduleDetail.isEmpty()) {
                    schedules.add(new GptSchedule(currentDate, currentScheduleDetail));
                    currentScheduleDetail = new ArrayList<>();
                }
                currentDate = dateMatcher.group(0);
            } else if (!line.trim().isEmpty()) {
                Matcher detailMatcher = detailPattern.matcher(line.trim());
                if (detailMatcher.find()) {
                    String detail = detailMatcher.group(1).trim();
                    Double latitude = formatCoordinate(Double.valueOf(detailMatcher.group(2)));
                    Double longitude = formatCoordinate(Double.valueOf(detailMatcher.group(3)));
                    Coordinate coordinate = new Coordinate(latitude, longitude);
                    currentScheduleDetail.add(new GptScheduleDetail(detail, coordinate));
                }
            }
        }

        if (!currentDate.isEmpty() && !currentScheduleDetail.isEmpty()) {
            schedules.add(new GptSchedule(currentDate, currentScheduleDetail));
        }

        return schedules;
    }

    private Double formatCoordinate(Double coordinate) {
        return Double.valueOf(String.format("%.8f", coordinate));
    }
}