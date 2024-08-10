package com.isp.backend.global.exception.receipt;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class ReceiptNotFoundException extends CustomException {

    public ReceiptNotFoundException() {
        super(ErrorCode.RECEIPT_NOT_FOUND);
    }

}
