package com.isp.backend.domain.schedule.entity;

import com.isp.backend.domain.country.entity.Country;
import com.isp.backend.domain.member.entity.Member;
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
@Table(name="schedules")
public class Schedule extends BaseEntity {

    @Id
    @Column(name="id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(name = "schedule_name", nullable = false)
    private String scheduleName ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<ScheduleDetail> scheduleDetails = new ArrayList<>();

    // FK 연결 필요
    @Column(name = "booking_id")
    private String bookingId ;

    @Column(name = "start_date")
    private String startDate ;

    @Column(name = "end_date")
    private String endDate ;

    @Column(name = "total_price")
    private double totalPrice ;

    @Builder.Default
    @Column(name = "activated", nullable = false)
    private boolean activated = true;

//    // setScheduleDetails 메서드 추가
//    public void setScheduleDetails(List<ScheduleDetail> scheduleDetails) {
//        this.scheduleDetails.clear();
//        if (scheduleDetails != null) {
//            this.scheduleDetails.addAll(scheduleDetails);
//        }
//    }

}
