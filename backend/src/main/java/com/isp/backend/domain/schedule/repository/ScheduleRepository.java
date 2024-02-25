package com.isp.backend.domain.schedule.repository;

import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
//    List<Schedule> findByMember(Member findmember);
    Optional<Schedule> findByIdAndActivatedIsTrue(Long scheduleId);

    List<Schedule> findByMemberAndActivatedIsTrue(Member findmember);
}
