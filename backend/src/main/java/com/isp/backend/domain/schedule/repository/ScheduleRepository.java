package com.isp.backend.domain.schedule.repository;

import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findById(Long id);
    Optional<Schedule> findByIdAndActivatedIsTrue(Long scheduleId);
    @EntityGraph(attributePaths = {"checkLists"})
    @Query("SELECT s FROM Schedule s LEFT JOIN FETCH s.country WHERE s.member = :member AND s.activated = true ORDER BY s.updatedAt DESC")
    List<Schedule> findSchedulesByMember(@Param("member") Member member);
    @EntityGraph(attributePaths = {"checkLists"})
    @Query("SELECT s FROM Schedule s LEFT JOIN FETCH s.scheduleDetails WHERE s.member = :member ORDER BY s.id DESC")
    List<Schedule> findTop5ByMemberOrderByIdDescWithDetails(@Param("member") Member member);


}
