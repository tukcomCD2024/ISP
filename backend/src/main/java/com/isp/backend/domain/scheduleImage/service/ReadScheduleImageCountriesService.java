package com.isp.backend.domain.scheduleImage.service;

import com.isp.backend.domain.schedule.dto.response.ScheduleListResponse;
import com.isp.backend.domain.scheduleImage.dto.response.ReadScheduleImageCountriesResponse;

import java.util.List;

public interface ReadScheduleImageCountriesService {
    ReadScheduleImageCountriesResponse readCountries(List<ScheduleListResponse> uid);
}