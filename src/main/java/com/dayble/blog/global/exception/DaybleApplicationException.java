package com.dayble.blog.global.exception;

import org.springframework.http.HttpStatus;

public class DaybleApplicationException extends RuntimeException {

    private final ErrorCodes errorCodes;

    public DaybleApplicationException(ErrorCodes errorCodes) {
        this.errorCodes = errorCodes;
    }

    public ErrorCodes getErrorCodes() {
        return errorCodes;
    }

    public HttpStatus getHttpStatus() {
        return errorCodes.status;
    }
}
