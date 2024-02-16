package com.isp.backend.domain.schedule.entity;

import com.isp.backend.domain.schedules.entity.Schedules;
import com.isp.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="schedule")
public class Schedule {

    @Id
    @Column(name="id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(name = "todo")
    private String todo ;

    @Column(name = "num")
    private int num ;

    @Column(name = "date")
    private String date ;

    // Schedules 엔티티의 필드를 참조하도록 수정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedules_id", nullable = false)
    private Schedules schedules;

}
