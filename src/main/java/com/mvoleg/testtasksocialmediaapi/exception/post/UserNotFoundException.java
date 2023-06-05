package com.mvoleg.testtasksocialmediaapi.exception.post;

import com.mvoleg.testtasksocialmediaapi.exception.ApiError;
import com.mvoleg.testtasksocialmediaapi.exception.BaseException;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(String username) {
        super(
                HttpStatus.NOT_FOUND,
                new ApiError()
                        .setStatusCode(HttpStatus.NOT_FOUND.value())
                        .setMessage("Пользователь с именем " + username + " не найден")
                        .setTimestamp(ZonedDateTime.now())
        );
    }

    public UserNotFoundException(long userId) {
        super(
                HttpStatus.NOT_FOUND,
                new ApiError()
                        .setStatusCode(HttpStatus.NOT_FOUND.value())
                        .setMessage("Пользователь с id " + userId + " не найден")
                        .setTimestamp(ZonedDateTime.now())
        );
    }
}
