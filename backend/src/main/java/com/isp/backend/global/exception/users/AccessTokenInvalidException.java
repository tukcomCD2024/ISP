package com.isp.backend.global.exception.users;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class AccessTokenInvalidException extends CustomException {
    public AccessTokenInvalidException() {
        super(ErrorCode.ACCESS_TOKEN_IS_INVALID);
    }
}