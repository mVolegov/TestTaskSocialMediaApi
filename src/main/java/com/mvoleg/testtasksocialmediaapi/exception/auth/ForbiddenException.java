package com.mvoleg.testtasksocialmediaapi.exception.auth;

import com.mvoleg.testtasksocialmediaapi.exception.ApiError;
import com.mvoleg.testtasksocialmediaapi.exception.BaseException;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ForbiddenException extends BaseException {

    public ForbiddenException(String message) {
        super(
                HttpStatus.FORBIDDEN,
                new ApiError()
                        .setStatusCode(HttpStatus.FORBIDDEN.value())
                        .setMessage(message)
                        .setTimestamp(ZonedDateTime.now())
        );
    }
}
