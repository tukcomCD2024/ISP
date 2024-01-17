package com.isp.backend.domain.schedules.repository;

import com.isp.backend.domain.schedules.entity.SharedSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharedScheduleRepository extends JpaRepository<SharedSchedule, String> {

}
