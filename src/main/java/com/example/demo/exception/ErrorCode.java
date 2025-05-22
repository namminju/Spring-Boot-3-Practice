package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON-400", "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-500", "서버 오류가 발생했습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-404", "사용자를 찾을 수 없습니다."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "AUTH-401", "비밀번호가 일치하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
