package com.isp.backend.domain.receipt.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SaveReceiptRequest {

    private Long scheduleId ;

    private String storeName;

    private String storeType ;

    private double totalPrice ;

    private String purchaseDate ;

    private List<ReceiptDetailRequest> receiptDetails;

}
