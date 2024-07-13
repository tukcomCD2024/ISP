package com.isp.backend.global.exception.openApi;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class ExchangeRateIsFailedException extends CustomException {

    public ExchangeRateIsFailedException() {
        super(ErrorCode.EXCHANGE_RATE_IS_FAILED);
    }

}