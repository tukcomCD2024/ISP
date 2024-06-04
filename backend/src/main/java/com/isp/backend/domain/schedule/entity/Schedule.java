package com.isp.backend.domain.schedule.entity;

import com.isp.backend.domain.country.entity.Country;
import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.domain.scheduleDetail.entity.CheckList;
import com.isp.backend.domain.scheduleDetail.entity.ScheduleDetail;
import com.isp.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@AllArgsConstructor
@Entity
@Builder
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "schedule")
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String scheduleName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<ScheduleDetail> scheduleDetails = new ArrayList<>();

    private String startDate;

    private String endDate;

    private double totalPrice;

    @Builder.Default
    @Column(nullable = false)
    private boolean activated = true;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CheckList> checkLists = new ArrayList<>();


}
