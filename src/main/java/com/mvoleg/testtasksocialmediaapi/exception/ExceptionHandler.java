package com.mvoleg.testtasksocialmediaapi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<ApiError> handleBaseException(BaseException baseException) {
        ApiError apiError = baseException.getApiError();

        return new ResponseEntity<>(apiError, baseException.getHttpStatus());
    }
}
