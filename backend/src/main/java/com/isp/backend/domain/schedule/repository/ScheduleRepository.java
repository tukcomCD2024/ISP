package com.isp.backend.domain.schedule.repository;

import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule findByIdAndActivatedIsTrue(Long scheduleId);

    List<Schedule> findByMemberAndActivatedIsTrue(Member findmember);
}
