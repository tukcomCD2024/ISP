package com.isp.backend.domain.receipt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReceiptListResponse {

    private Long receiptId;

    private String storeName;

    private String storeType ;

    private double price ; // receipt 테이블의 totalPrice

    private int orderNum ;

    private String purchaseDate ;

}
