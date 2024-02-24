package com.isp.backend.domain.schedule.repository;

import com.isp.backend.domain.schedule.entity.SharedSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharedScheduleRepository extends JpaRepository<SharedSchedule, String> {

}
