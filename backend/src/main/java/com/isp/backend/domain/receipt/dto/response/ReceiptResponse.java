package com.isp.backend.domain.receipt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReceiptResponse {

    private Long receiptId ;

    private String purchaseDate ;

    private int orderNum ;

    private String receiptImg ;

    private double totalPrice ;

    private List<ReceiptDetailResponse> receiptDetailList;
}
