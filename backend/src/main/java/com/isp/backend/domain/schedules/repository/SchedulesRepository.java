package com.isp.backend.domain.schedules.repository;

import com.isp.backend.domain.schedules.entity.Schedules;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulesRepository extends JpaRepository<Schedules, String> {

}
