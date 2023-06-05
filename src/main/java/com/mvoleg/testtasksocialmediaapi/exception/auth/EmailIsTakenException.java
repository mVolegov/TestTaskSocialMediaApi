package com.mvoleg.testtasksocialmediaapi.exception.auth;

import com.mvoleg.testtasksocialmediaapi.exception.ApiError;
import com.mvoleg.testtasksocialmediaapi.exception.BaseException;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class EmailIsTakenException extends BaseException {

    public EmailIsTakenException(String email) {
        super(
                HttpStatus.BAD_REQUEST,
                new ApiError()
                        .setStatusCode(HttpStatus.BAD_REQUEST.value())
                        .setMessage("Пользователь с email " + email + " уже существует")
                        .setTimestamp(ZonedDateTime.now())
        );
    }
}
