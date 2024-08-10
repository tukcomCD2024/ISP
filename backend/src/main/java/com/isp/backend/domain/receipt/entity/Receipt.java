package com.isp.backend.domain.receipt.entity;

import com.isp.backend.domain.schedule.entity.Schedule;
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
@Table(name = "receipt")
public class Receipt extends BaseEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Schedule schedule;

    private String storeName;

    @Enumerated(EnumType.STRING)
    private StoreType storeType;

    private double totalPrice;

    private String purchaseDate;

    private String receiptImg;

    private int orderNum ;

}
