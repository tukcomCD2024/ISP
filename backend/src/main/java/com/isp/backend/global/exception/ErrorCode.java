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

    // Image
    DIRECTORY_NAME_NOTFOUND(HttpStatus.NOT_FOUND,"I001","S3에서 해당 디렉토리의 이름을 찾을 수 없습니다."),

    // Schedule
    COUNTRY_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "여행할 국가를 찾을 수 없습니다."),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "S002", "여행 일정을 찾을 수 없습니다."),
    NOT_YOUR_SCHEDULE(HttpStatus.UNAUTHORIZED, "S003", "사용자의 여행 일정이 아닙니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "S004", "이미지를 찾을 수 없습니다."),
    IATA_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "S005", "해당 국가의 공항 코드를 찾을 수 없습니다."),

    // Flight
    FLIGHT_SEARCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"F001", "힝공편 조회를 가져오는 중 오류를 발생했습니다."),
    SKY_SCANNER_GENERATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"F002", "스카이스캐너 URL을 생성할 수 없습니다."),
    FLIGHT_NOT_FOUND(HttpStatus.NOT_FOUND, "F003", "해당 id의 항공권을 찾을 수 없습니다."),
    NOT_YOUR_FLIGHT(HttpStatus.UNAUTHORIZED, "F004", "사용자의 항공권이 아닙니다");


    private HttpStatus status;
    private String code;
    private String message;


    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
