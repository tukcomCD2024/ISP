package com.isp.backend.domain.receipt.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@Entity
@Builder
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "receipt_detail")
public class ReceiptDetail {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", nullable = false)
    private Receipt receipt;

    private String item;

    private int count;

    private double itemPrice;

}
