package com.isp.backend.domain.receipt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScheduleListWithReceiptResponse {

    private String scheduleName;

    private String startDate;

    private String endDate;

    private String currencyName;

    private double totalReceiptsPrice ;

    private int receiptCount ; // 영수증 개수

}
