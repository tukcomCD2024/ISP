package com.isp.backend.domain.flight.entity;

import com.isp.backend.domain.country.entity.Country;
import com.isp.backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor
@Table(name = "flight")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private String carrierCode ;

    private Double price ;

    private String abroadDuration ;

    private String abroadDepartureTime ; // 출국-출발

    private String abroadArrivalTime ;  // 출국-도착

    @ManyToOne
    @JoinColumn(name = "departure_iata_code_id")
    private Country departureIataCode ;

    private String homeDuration ;

    private String homeDepartureTime ;  // 입국-출발

    private String homeArrivalTime ;  // 입국-도착
    @ManyToOne
    @JoinColumn(name = "arrival_iata_code_id")
    private Country arrivalIataCode ;

    private int transferCount ;

    private int adult ;

    private int children ;


}
