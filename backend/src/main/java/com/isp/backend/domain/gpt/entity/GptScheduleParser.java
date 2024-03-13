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
        Pattern datePattern = Pattern.compile(ParsingConstants.DATE_REGEX);

        List<String> lines = List.of(scheduleText.split(ParsingConstants.NEW_LINE_REGEX));
        List<String> currentScheduleDetail = new ArrayList<>();
        String currentDate = ParsingConstants.CURRENT_DATE;

        for (String line : lines) {
            Matcher dateMatcher = datePattern.matcher(line);
            if (dateMatcher.find()) {
                if (!currentDate.isEmpty()) {
                    schedules.add(new GptSchedule(currentDate, currentScheduleDetail));
                    currentScheduleDetail = new ArrayList<>();
                }
                currentDate = dateMatcher.group(ParsingConstants.GROUP_MATCH);
            } else if (!line.trim().isEmpty() && ParsingConstants.FILTER_STRINGS.stream().noneMatch(line::contains)) {
                currentScheduleDetail.add(line.trim().substring(ParsingConstants.BEGIN_INDEX)); // Remove leading index
            }
        }

        if (!currentDate.isEmpty()) {
            schedules.add(new GptSchedule(currentDate, currentScheduleDetail));
        }

        return schedules;
    }

}
