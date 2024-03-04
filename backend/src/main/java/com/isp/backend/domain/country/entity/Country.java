package com.isp.backend.domain.country.entity;

import com.isp.backend.domain.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor
@Table(name="country")
public class Country {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nation")
    private String nation;

    @Column(name = "city")
    private String city;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "airport_code")
    private String airportCode;

    @Column(name = "latitude")  // 위도
    private double latitude ;

    @Column(name = "longitude")  // 경도
    private double longitude ;

    @OneToMany (mappedBy = "country")
    private List<Schedule> schedules;

}
