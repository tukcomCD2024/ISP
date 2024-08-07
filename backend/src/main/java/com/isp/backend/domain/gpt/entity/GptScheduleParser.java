package com.isp.backend.domain.gpt.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class GptScheduleParser {

    public List<GptSchedule> parseScheduleText(String scheduleText) {
        System.out.println(scheduleText);
        List<GptSchedule> schedules = new ArrayList<>();

        String[] entries = scheduleText.split("<");

        for (String entry : entries) {
            if (entry.trim().isEmpty()) continue;

            String[] lines = entry.split("\n");
            String date = lines[0].trim().replace(">", "");

            List<GptScheduleDetail> scheduleDetails = new ArrayList<>();

            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(" ");
                StringBuilder detail = new StringBuilder();
                double latitude = 0.0;
                double longitude = 0.0;

                for (int j = 0; j < parts.length; j++) {
                    try {
                        if (j == parts.length - 2) {
                            latitude = Double.parseDouble(parts[j].replace(",", ""));
                        } else if (j == parts.length - 1) {
                            longitude = Double.parseDouble(parts[j]);
                        } else {
                            if (!detail.isEmpty()) detail.append(" ");
                            detail.append(parts[j]);
                        }
                    } catch (NumberFormatException e) {
                        if (!detail.isEmpty()) detail.append(" ");
                        detail.append(parts[j]);
                    }
                }

                String detailString = detail.toString().trim();
                if (detailString.startsWith("-")) {
                    detailString = detailString.substring(1).trim();
                }

                scheduleDetails.add(new GptScheduleDetail(detailString, new Coordinate(latitude, longitude)));
            }

            schedules.add(new GptSchedule(date, scheduleDetails));
        }

        return schedules;
    }
}