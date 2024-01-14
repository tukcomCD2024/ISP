package com.isp.backend.domain.travelPlace.entity;

import com.isp.backend.domain.schedules.entity.MySchedule;
import com.isp.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="travel_place")
public class TravelPlace extends BaseEntity {

    @Id
    @Column(name="id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(name = "place_name")
    private String placeName ;

    @Column(name = "country")
    private String country ;

    @Column(name = "city")
    private String city ;

    @Column(name = "longitude") // 경도
    private double longitude ;

    @Column(name = "latitude")  // 위도
    private double latitude ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_schedule_id", nullable = false)
    private MySchedule mySchedule;

}
