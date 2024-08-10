package com.isp.backend.global.exception.schedule;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class NotYourScheduleException extends CustomException {

    public NotYourScheduleException() {
        super(ErrorCode.NOT_YOUR_SCHEDULE);
    }

}
