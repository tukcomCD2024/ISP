package com.isp.backend.domain.scheduleDetail.entity;

import com.isp.backend.domain.schedule.entity.Schedule;
import com.isp.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@Entity
@Builder
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "checkList")
public class CheckList extends BaseEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String todo ;

    @Column(nullable = false)
    private boolean isChecked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

}
