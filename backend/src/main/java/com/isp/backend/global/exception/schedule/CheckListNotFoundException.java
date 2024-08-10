package com.isp.backend.global.exception.schedule;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

import java.util.function.Supplier;


public class CheckListNotFoundException extends CustomException {

    public CheckListNotFoundException() {
        super(ErrorCode.CHECK_LIST_NOT_FOUND);
    }

}
