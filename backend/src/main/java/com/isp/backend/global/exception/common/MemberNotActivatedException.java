package com.isp.backend.global.exception.common;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class MemberNotActivatedException extends CustomException {

    public MemberNotActivatedException() {
        super(ErrorCode.MEMBER_NOT_ACTIVATED);
    }

}

