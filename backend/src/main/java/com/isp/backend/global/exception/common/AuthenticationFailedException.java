package com.isp.backend.global.exception.common;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class AuthenticationFailedException  extends CustomException {

    public AuthenticationFailedException() {super(ErrorCode.AUTHENTICATION_FAILED);}

}
