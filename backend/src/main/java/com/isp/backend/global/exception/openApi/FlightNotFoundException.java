package com.isp.backend.global.exception.openApi;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class FlightNotFoundException extends CustomException {

    public FlightNotFoundException() {super(ErrorCode.FLIGHT_NOT_FOUND);}

}
