package com.isp.backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {


    // Common
    MEMBER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "U001", "사용자를 찾을 수 없습니다."),
    SOCIAL_AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "U002", "사용자가 OAuth2 로그인에 실패하였습니다."),
    MEMBER_NOT_ACTIVATED(HttpStatus.BAD_REQUEST, "U003", "사용자가 활성화 상태가 아닙니다."),
    ACCESS_TOKEN_IS_INVALID(HttpStatus.UNAUTHORIZED, "U004", "엑세스 토큰이 유효하지 않습니다."),
    REFRESH_TOKEN_IS_INVALID(HttpStatus.UNAUTHORIZED, "U005", "리프레시 토큰이 유효하지 않습니다."),
    COUNTRY_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "여행할 국가를 찾을 수 없습니다."),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "S002", "여행 일정을 찾을 수 없습니다."),
    NOT_YOUR_SCHEDULE(HttpStatus.UNAUTHORIZED, "S003", "사용자의 여행 일정이 아닙니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "S004", "이미지를 찾을 수 없습니다.");


    private HttpStatus status;
    private String code;
    private String message;


    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
