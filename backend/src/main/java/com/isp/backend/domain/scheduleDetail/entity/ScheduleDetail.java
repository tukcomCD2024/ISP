package com.isp.backend.domain.scheduleDetail.entity;

import com.isp.backend.domain.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@Entity
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="schedule_detail")
public class ScheduleDetail {

    @Id
    @Column(name="id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(name = "todo")  // 할 일
    private String todo ;

    @Column(name = "date")
    private String date ;

    @Column(name = "place")  // 장소
    private String place ;

    @Column(name = "num")  // 일정 순서
    private int num ;

    @Column(name = "budget")  // 예산
    private double budget ;

    @Column(name = "schedule_type", nullable = true)  // 일정 유형
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType ;

    @Column(name = "latitude")  // 위도
    private double latitude ;

    @Column(name = "longitude")  // 경도
    private double longitude ;

    // Schedules 엔티티의 필드를 참조하도록 수정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedules_id", nullable = false)
    private Schedule schedule;

}
