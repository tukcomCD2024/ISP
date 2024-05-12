package com.isp.backend.global.exception.flight;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class FlightSearchFailedException extends CustomException {

    public FlightSearchFailedException() {
        super(ErrorCode.FLIGHT_SEARCH_FAILED);
    }

}