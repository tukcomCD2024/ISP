package com.isp.backend.global.exception.common;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class RefreshTokenInvalidException extends CustomException {

    public RefreshTokenInvalidException() {
        super(ErrorCode.REFRESH_TOKEN_IS_INVALID);
    }

}
