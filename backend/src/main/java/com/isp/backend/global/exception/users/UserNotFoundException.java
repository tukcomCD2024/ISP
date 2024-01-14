package com.isp.backend.global.exception.users;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class UserNotFoundException extends CustomException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}

