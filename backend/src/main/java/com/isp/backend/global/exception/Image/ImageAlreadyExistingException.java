package com.isp.backend.global.exception.Image;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;


public class ImageAlreadyExistingException extends CustomException {

    public ImageAlreadyExistingException() {
        super(ErrorCode.IMAGE_ALREADY_EXISTING);
    }

}