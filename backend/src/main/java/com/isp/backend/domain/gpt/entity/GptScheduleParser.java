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
        Pattern pattern = Pattern.compile(ParsingConstants.DATE_REGEX);

        List<String> lines = List.of(scheduleText.split(ParsingConstants.NEW_LINE_REGEX));
        List<String> currentScheduleDetail = new ArrayList<>();
        String currentDate = ParsingConstants.CURRENT_DATE;

        for (String line : lines) {
            processLine(line, pattern, schedules, currentScheduleDetail, currentDate);
        }

        addScheduleIfNotEmpty(currentDate, schedules, currentScheduleDetail);

        return schedules;
    }

    private void processLine(String line, Pattern pattern, List<GptSchedule> schedules, List<String> currentScheduleDetail, String currentDate) {
        Matcher dateMatcher = pattern.matcher(line);

        if (dateMatcher.find()) {
            handleNewDate(currentDate, schedules, currentScheduleDetail, dateMatcher.group(1));
        } else {
            handleNonDateLine(line, currentScheduleDetail);
        }
    }

    private void handleNewDate(String currentDate, List<GptSchedule> schedules, List<String> currentScheduleDetail, String newDate) {
        if (!currentDate.isEmpty()) {
            schedules.add(new GptSchedule(currentDate, currentScheduleDetail));
        }
    }

    private void handleNonDateLine(String line, List<String> currentScheduleDetail) {
        if (!line.trim().isEmpty() && ParsingConstants.FILTER_STRINGS.stream().noneMatch(line::contains)) {
            currentScheduleDetail.add(line.trim().substring(ParsingConstants.BEGIN_INDEX)); // Remove leading index
        }
    }

    private void addScheduleIfNotEmpty(String currentDate, List<GptSchedule> schedules, List<String> currentScheduleDetail) {
        if (!currentDate.isEmpty()) {
            schedules.add(new GptSchedule(currentDate, currentScheduleDetail));
        }
    }
}
