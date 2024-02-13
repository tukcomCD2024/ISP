package com.isp.backend.domain.receipt.entity;

import com.isp.backend.domain.schedules.entity.Schedules;
import com.isp.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="receipt")
public class Receipt extends BaseEntity {

    @Id
    @Column(name="id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "schedule_id", nullable = false)
//    private Schedules schedules;

    @Column(name = "title")
    private String title ;

    @Column(name = "price")
    private double price ;

    @Column(name = "purchase_date")
    private String purchaseDate ;
}
