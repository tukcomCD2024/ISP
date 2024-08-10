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
public class ScheduleReceiptResponse {

    private String scheduleName;

    private String startDate;

    private String endDate;

    private String currencyName;  // schedule table -> country table의 currency_name 컬럼

    private double totalReceiptsPrice ;

    private List<ReceiptListResponse> receiptList;

}
