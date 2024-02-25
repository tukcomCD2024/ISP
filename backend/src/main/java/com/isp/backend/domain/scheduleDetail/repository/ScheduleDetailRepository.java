package com.isp.backend.domain.scheduleDetail.repository;

import com.isp.backend.domain.schedule.entity.Schedule;
import com.isp.backend.domain.scheduleDetail.entity.ScheduleDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleDetailRepository extends JpaRepository<ScheduleDetail, Long> {

    void deleteBySchedule(Schedule schedule);
}