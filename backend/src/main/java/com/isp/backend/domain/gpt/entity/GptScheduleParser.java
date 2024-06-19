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
        List<GptSchedule> schedules = new ArrayList<>();
        List<String> lines = List.of(scheduleText.split(ParsingConstants.NEW_LINE_REGEX));
        List<GptScheduleDetail> currentScheduleDetail = new ArrayList<>();
        String currentDate = ParsingConstants.CURRENT_DATE;

        for (String line : lines) {
            currentDate = processLine(line, currentDate, currentScheduleDetail, schedules);
        }

        if (!currentDate.isEmpty()) {
            addSchedule(schedules, currentDate, currentScheduleDetail);
        }

        return schedules;
    }

    private String processLine(String line, String currentDate, List<GptScheduleDetail> currentScheduleDetail, List<GptSchedule> schedules) {
        if (isDateLine(line)) {
            if (!currentDate.isEmpty()) {
                addSchedule(schedules, currentDate, currentScheduleDetail);
                currentScheduleDetail.clear();
            }
            return extractDate(line);
        } else if (shouldProcessLine(line)) {
            addDetail(line, currentScheduleDetail);
        }
        return currentDate;
    }

    private boolean isDateLine(String line) {
        Pattern datePattern = Pattern.compile(ParsingConstants.DATE_REGEX);
        Matcher dateMatcher = datePattern.matcher(line);
        return dateMatcher.find();
    }

    private String extractDate(String line) {
        Pattern datePattern = Pattern.compile(ParsingConstants.DATE_REGEX);
        Matcher dateMatcher = datePattern.matcher(line);
        if (dateMatcher.find()) {
            return dateMatcher.group(ParsingConstants.GROUP_MATCH);
        }
        return ParsingConstants.CURRENT_DATE;
    }

    private boolean shouldProcessLine(String line) {
        return !line.trim().isEmpty() && ParsingConstants.FILTER_STRINGS.stream().noneMatch(line::contains);
    }

    private void addDetail(String line, List<GptScheduleDetail> currentScheduleDetail) {
        Pattern detailPattern = Pattern.compile("^(?:\\d+\\.\\s*)?(.*?)(\\d+\\.\\d{1,8}),\\s*(\\d+\\.\\d{1,8})$");
        Matcher detailMatcher = detailPattern.matcher(line.trim());
        if (detailMatcher.find()) {
            String detail = detailMatcher.group(1).trim();
            Double latitude = Double.valueOf(detailMatcher.group(2));
            Double longitude = Double.valueOf(detailMatcher.group(3));

            String formattedLatitude = String.format("%.8f", latitude);
            String formattedLongitude = String.format("%.8f", longitude);

            Coordinate coordinate = new Coordinate(Double.parseDouble(formattedLatitude), Double.parseDouble(formattedLongitude));
            currentScheduleDetail.add(new GptScheduleDetail(detail, coordinate));
        }
    }

    private void addSchedule(List<GptSchedule> schedules, String date, List<GptScheduleDetail> details) {
        schedules.add(new GptSchedule(date, details));
    }
}