package com.isp.backend.global.exception.schedule;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class CountryNotFoundException extends CustomException {

    public CountryNotFoundException() {
        super(ErrorCode.COUNTRY_NOT_FOUND);
    }

}
