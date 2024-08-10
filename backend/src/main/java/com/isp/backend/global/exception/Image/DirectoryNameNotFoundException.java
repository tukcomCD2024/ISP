package com.isp.backend.global.exception.Image;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;


public class DirectoryNameNotFoundException extends CustomException {

    public DirectoryNameNotFoundException() {
        super(ErrorCode.DIRECTORY_NAME_NOTFOUND);
    }

}