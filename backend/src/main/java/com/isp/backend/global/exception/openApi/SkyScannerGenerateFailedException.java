package com.isp.backend.global.exception.openApi;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class SkyScannerGenerateFailedException extends CustomException {

    public SkyScannerGenerateFailedException() {
        super(ErrorCode.SKY_SCANNER_GENERATE_FAILED);
    }

}