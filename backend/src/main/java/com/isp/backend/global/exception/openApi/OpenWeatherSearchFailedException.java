package com.isp.backend.global.exception.openApi;

import com.isp.backend.global.exception.CustomException;
import com.isp.backend.global.exception.ErrorCode;

public class OpenWeatherSearchFailedException extends CustomException {

    public OpenWeatherSearchFailedException() {
        super(ErrorCode.OPEN_WEATHER_SEARCH_FAILED);
    }

}