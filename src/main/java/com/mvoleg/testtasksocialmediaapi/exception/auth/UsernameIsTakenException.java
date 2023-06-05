package com.mvoleg.testtasksocialmediaapi.exception.auth;

import com.mvoleg.testtasksocialmediaapi.exception.ApiError;
import com.mvoleg.testtasksocialmediaapi.exception.BaseException;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class UsernameIsTakenException extends BaseException {

    public UsernameIsTakenException(String username) {
        super(
                HttpStatus.BAD_REQUEST,
                new ApiError()
                        .setStatusCode(HttpStatus.BAD_REQUEST.value())
                        .setMessage("Имя пользователя " + username + " уже занято")
                        .setTimestamp(ZonedDateTime.now())
        );
    }
}
