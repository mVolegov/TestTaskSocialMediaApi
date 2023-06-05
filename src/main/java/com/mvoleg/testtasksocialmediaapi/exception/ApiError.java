package com.mvoleg.testtasksocialmediaapi.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ApiError {

    private int statusCode;
    private String message;
    private ZonedDateTime timestamp;

    public ApiError() {
        this.statusCode = HttpStatus.BAD_REQUEST.value();
        this.message = "Default error message";
        this.timestamp = ZonedDateTime.now();
    }

    public ApiError(int statusCode, String message, ZonedDateTime timestamp) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public ApiError setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ApiError setMessage(String message) {
        this.message = message;
        return this;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public ApiError setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
