package com.isp.backend.domain.schedule.entity;

import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "shared_schedule")
public class SharedSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedules_id", nullable = false)  // 컬럼명 삭제X
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

}
