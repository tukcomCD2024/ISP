package com.isp.backend.domain.receipt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReceiptDetailResponse {

    private String item;

    private int count;

    private double itemPrice;

}
