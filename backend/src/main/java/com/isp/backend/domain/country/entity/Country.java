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
    private Long id;

    private String nation;

    private String city;

    private String imageUrl;

    @OneToMany(mappedBy = "country")
    private List<Schedule> schedules;

}
