package com.isp.backend.global.exception.openApi;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class ExchangeRateSearchFailedException extends CustomException {

    public ExchangeRateSearchFailedException() {
        super(ErrorCode.EXCHANGE_RATE_SEARCH_FAILED);
    }

}