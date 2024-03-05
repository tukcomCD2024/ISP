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
@Table(name = "schedule_detail")
public class ScheduleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String todo;

    private String date;

    private String place;

    private int num;

    private double budget;

    private double latitude;

    private double longitude;

    // Schedules 엔티티의 필드를 참조하도록 수정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Schedule schedule;

}
