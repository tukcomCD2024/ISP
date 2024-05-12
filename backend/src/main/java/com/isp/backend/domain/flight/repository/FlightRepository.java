package com.isp.backend.domain.flight.repository;

import com.isp.backend.domain.flight.entity.Flight;
import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    @Query("SELECT f FROM Flight f WHERE f.member = :member ORDER BY f.id DESC")
    List<Flight> findByMember(@Param("member") Member member);

}
