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
    
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType ;

    private double budget;

    private double latitude;

    private double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id" , nullable = false)  // 컬럼명 삭제X
    private Schedule schedule;

}
