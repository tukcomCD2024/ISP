package com.isp.backend.global.exception.openApi;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class NotYourFlightException extends CustomException {

    public NotYourFlightException() {
        super(ErrorCode.NOT_YOUR_FLIGHT);
    }

}


