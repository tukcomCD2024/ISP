package com.isp.backend.domain.schedule.service;

import com.isp.backend.domain.country.entity.Country;
import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.domain.schedule.dto.response.ScheduleListResponse;
import com.isp.backend.domain.schedule.dto.request.ScheduleSaveRequest;
import com.isp.backend.domain.schedule.entity.Schedule;

import java.util.List;

public interface ScheduleService {

    void saveSchedule(String uid, ScheduleSaveRequest scheduleSaveRequest);

    List<ScheduleListResponse> getScheduleList(String uid);

    ScheduleSaveRequest getScheduleDetail(String uid, Long scheduleId);

    void deleteSchedule(String uid, Long scheduleId);

    ScheduleSaveRequest updateSchedule(String uid, Long scheduleId, ScheduleSaveRequest updateRequestDTO);

    void calculateTotalPrice(Schedule schedule);

    Member validateUserCheck(String uid);

    Schedule validateSchedule(Long scheduleId);

    Country validateCountry(String countryName);
}
