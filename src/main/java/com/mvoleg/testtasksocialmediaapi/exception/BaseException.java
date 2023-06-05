package com.mvoleg.testtasksocialmediaapi.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {

    private HttpStatus httpStatus;
    private ApiError apiError;

    public BaseException() {
    }

    public BaseException(HttpStatus httpStatus, ApiError apiError) {
        super(apiError.getMessage());

        this.httpStatus = httpStatus;
        this.apiError = apiError;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ApiError getApiError() {
        return apiError;
    }
}
