package com.isp.backend.domain.country.entity;

import com.isp.backend.domain.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor
@Table(name = "country")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nation;

    private String city;

    private String imageUrl;

    private String airportCode;

    private double latitude ;  // 위도

    private double longitude ;  // 경도

    @OneToMany (mappedBy = "country")
    private List<Schedule> schedules;

}
