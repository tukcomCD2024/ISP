package com.isp.backend.domain.country.entity;

import com.isp.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;


@Getter
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor
@Table(name = "exchange_rate")
public class ExchangeRate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromCurrency;

    private String toCurrency;

    private BigDecimal rate;

}