package com.isp.backend.domain.receipt.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReceiptDetailRequest {

    private String item;

    private int count;

    private double itemPrice;

}
