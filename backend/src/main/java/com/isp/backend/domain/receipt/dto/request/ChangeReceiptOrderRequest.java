package com.isp.backend.domain.receipt.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChangeReceiptOrderRequest {

    private Long receiptId ;
    private String purchaseDate ;
    private int orderNum ;

}
