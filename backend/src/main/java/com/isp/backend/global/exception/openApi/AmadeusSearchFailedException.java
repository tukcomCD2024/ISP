package com.isp.backend.global.exception.openApi;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class AmadeusSearchFailedException extends CustomException {

    public AmadeusSearchFailedException() {
        super(ErrorCode.AMADEUS_SEARCH_FAILED);
    }

}