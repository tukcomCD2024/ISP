package com.isp.backend.domain.schedule.service;

import com.isp.backend.domain.country.entity.Country;
import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.domain.schedule.dto.response.FastestScheduleResponse;
import com.isp.backend.domain.schedule.dto.response.LatestCreateResponse;
import com.isp.backend.domain.schedule.dto.response.ScheduleListResponse;
import com.isp.backend.domain.schedule.dto.request.ScheduleSaveRequest;
import com.isp.backend.domain.schedule.entity.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleService {

    void saveSchedule(String uid, ScheduleSaveRequest scheduleSaveRequest);

    List<ScheduleListResponse> getScheduleList(String uid);

    ScheduleSaveRequest getScheduleDetail(String uid, Long scheduleId);

    void deleteSchedule(String uid, Long scheduleId);

    FastestScheduleResponse getFastestSchedule(String uid);

    ScheduleSaveRequest updateSchedule(String uid, Long scheduleId, ScheduleSaveRequest updateRequestDTO);

    Optional<Schedule> findClosestSchedule(List<Schedule> schedules, LocalDate today);

    void calculateTotalPrice(Schedule schedule);

    Member validateUserCheck(String uid);

    Schedule validateSchedule(Long scheduleId);

    Country validateCountry(String countryName);

    List<LatestCreateResponse> getLatestCreatedSchedules(String uid, int limit);
}
