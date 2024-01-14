package com.isp.backend.domain.schedules.repository;

import com.isp.backend.domain.schedules.entity.MySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyScheduleRepository extends JpaRepository<MySchedule, String> {

}
