package com.isp.backend.domain.scheduleDetail.repository;

import com.isp.backend.domain.schedule.entity.Schedule;
import com.isp.backend.domain.scheduleDetail.entity.CheckList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckListRepository extends JpaRepository<CheckList, Long> {
    List<CheckList> findBySchedule(Schedule schedule);
}
