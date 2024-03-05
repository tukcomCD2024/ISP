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
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nation")
    private String nation;

    @Column(name = "city")
    private String city;

    @Column(name = "imageUrl")
    private String imageUrl;

    @OneToMany(mappedBy = "country")
    private List<Schedule> schedules;

}
