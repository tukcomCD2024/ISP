package com.isp.backend.global.exception.users;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class UserEmailAlreadyExistsException extends CustomException {

    public UserEmailAlreadyExistsException() {
        super(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
}

