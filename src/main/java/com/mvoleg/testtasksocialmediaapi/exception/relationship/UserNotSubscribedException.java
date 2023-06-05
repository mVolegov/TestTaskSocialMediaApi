package com.mvoleg.testtasksocialmediaapi.exception.relationship;

import com.mvoleg.testtasksocialmediaapi.exception.ApiError;
import com.mvoleg.testtasksocialmediaapi.exception.BaseException;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class UserNotSubscribedException extends BaseException {

    public UserNotSubscribedException(String sender) {
        super(
                HttpStatus.BAD_REQUEST,
                new ApiError()
                        .setStatusCode(HttpStatus.BAD_REQUEST.value())
                        .setMessage("Пользователь " + sender + " не подписан")
                        .setTimestamp(ZonedDateTime.now())
        );
    }

    public UserNotSubscribedException(String sender, String receiver) {
        super(
                HttpStatus.BAD_REQUEST,
                new ApiError()
                        .setStatusCode(HttpStatus.BAD_REQUEST.value())
                        .setMessage("Пользователь " + sender + " не подписан на пользователя " + receiver)
                        .setTimestamp(ZonedDateTime.now())
        );
    }
}
