package com.isp.backend.global.exception.schedule;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class IataCodeNotFoundException extends CustomException {

    public IataCodeNotFoundException() {
        super(ErrorCode.IATA_CODE_NOT_FOUND);
    }

}
