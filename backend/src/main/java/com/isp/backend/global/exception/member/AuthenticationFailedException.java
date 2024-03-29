package com.isp.backend.global.exception.member;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class AuthenticationFailedException extends CustomException {

    public AuthenticationFailedException() {
        super(ErrorCode.SOCIAL_AUTHENTICATION_FAILED);
    }
}
