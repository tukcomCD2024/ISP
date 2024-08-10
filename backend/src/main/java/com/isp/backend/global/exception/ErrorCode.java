package com.isp.backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {


    // Common
    MEMBER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "U001", "사용자를 찾을 수 없습니다."),
    MEMBER_NOT_ACTIVATED(HttpStatus.BAD_REQUEST, "U002", "사용자가 활성화 상태가 아닙니다."),
    AUTHENTICATION_FAILED(HttpStatus.FORBIDDEN, "U003", "권한이 없는 요청입니다. 토큰을 추가해주세요."),
    ACCESS_TOKEN_IS_INVALID(HttpStatus.UNAUTHORIZED, "U004", "엑세스 토큰이 유효하지 않습니다."),
    REFRESH_TOKEN_IS_INVALID(HttpStatus.UNAUTHORIZED, "U005", "리프레시 토큰이 유효하지 않습니다."),

    // Image
    DIRECTORY_NAME_NOTFOUND(HttpStatus.NOT_FOUND,"I001","S3에서 해당 디렉토리의 이름을 찾을 수 없습니다."),
    IMAGE_ALREADY_EXISTING(HttpStatus.BAD_REQUEST, "I002", "이미지가 이미 저장되어 있습니다."),

    // Schedule
    COUNTRY_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "여행할 국가를 찾을 수 없습니다."),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "S002", "여행 일정을 찾을 수 없습니다."),
    NOT_YOUR_SCHEDULE(HttpStatus.UNAUTHORIZED, "S003", "사용자의 여행 일정이 아닙니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "S004", "이미지를 찾을 수 없습니다."),
    IATA_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "S005", "해당 국가의 공항 코드를 찾을 수 없습니다."),
    CHECK_LIST_NOT_FOUND(HttpStatus.NOT_FOUND, "S006", "체크리스트를 찾을 수 없습니다"),

    // Receipt
    RECEIPT_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "헤딩 영수증 ID를 찾을 수 없습니다."),

    // Open API
    AMADEUS_SEARCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"F001", "아마데우스 요청을 가져오는 중 오류를 발생했습니다."),
    SKY_SCANNER_GENERATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"F002", "스카이스캐너 URL을 생성할 수 없습니다."),
    FLIGHT_NOT_FOUND(HttpStatus.NOT_FOUND, "F003", "해당 id의 항공권을 찾을 수 없습니다."),
    NOT_YOUR_FLIGHT(HttpStatus.UNAUTHORIZED, "F004", "사용자의 항공권이 아닙니다"),
    OPEN_WEATHER_SEARCH_FAILED(HttpStatus.NOT_FOUND,"F005", "날씨 정보 파싱에 실패하였습니다"),
    EXCHANGE_RATE_SEARCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"F006", "환율 요청을 가져오는 중 오류를 발생했습니다."),
    EXCHANGE_RATE_IS_FAILED(HttpStatus.BAD_REQUEST,"F007", "환율 DB를 저장하던 중 오류가 발생하였습니다.");


    private HttpStatus status;
    private String code;
    private String message;


    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
