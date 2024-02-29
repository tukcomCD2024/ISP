package com.isp.backend.global.exception.schedule;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class ScheduleNotFoundException extends CustomException {

    public ScheduleNotFoundException() {
        super(ErrorCode.SCHEDULE_NOT_FOUND);
    }

}
