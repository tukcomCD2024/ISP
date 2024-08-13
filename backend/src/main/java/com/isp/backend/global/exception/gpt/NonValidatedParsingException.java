package com.isp.backend.global.exception.gpt;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class NonValidatedParsingException extends CustomException {
    public NonValidatedParsingException() {
        super(ErrorCode.PARSING_IS_NOT_VALIDATED);
    }
}