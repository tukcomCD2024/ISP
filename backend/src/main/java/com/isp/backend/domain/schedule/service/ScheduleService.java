package com.isp.backend.domain.schedule.service;

import com.isp.backend.domain.country.entity.Country;
import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.domain.schedule.dto.ScheduleListResponseDTO;
import com.isp.backend.domain.schedule.dto.ScheduleRequestDTO;
import com.isp.backend.domain.schedule.dto.ScheduleResponseDTO;
import com.isp.backend.domain.schedule.dto.ScheduleSaveRequestDTO;
import com.isp.backend.domain.schedule.entity.Schedule;

import java.util.List;

public interface ScheduleService {

    void saveSchedule(String uid, ScheduleSaveRequestDTO scheduleSaveRequestDTO);

    List<ScheduleListResponseDTO> getScheduleList(String uid);

    ScheduleSaveRequestDTO getScheduleDetail(String uid, Long scheduleId);

    void deleteSchedule(String uid, Long scheduleId);

    ScheduleSaveRequestDTO updateSchedule(String uid, Long scheduleId, ScheduleSaveRequestDTO updateRequestDTO);

    void calculateTotalPrice(Schedule schedule);

    Member validateUserCheck(String uid);

    Schedule validateSchedule(Long scheduleId);

    Country validateCountry(String countryName);
}
