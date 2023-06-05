package com.mvoleg.testtasksocialmediaapi.exception.relationship;

import com.mvoleg.testtasksocialmediaapi.exception.ApiError;
import com.mvoleg.testtasksocialmediaapi.exception.BaseException;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class UserAlreadySubscribedException extends BaseException {

    public UserAlreadySubscribedException(String sender, String receiver) {
        super(
                HttpStatus.BAD_REQUEST,
                new ApiError()
                        .setStatusCode(HttpStatus.BAD_REQUEST.value())
                        .setMessage("Пользователь " + sender + " уже подписан на пользователя " + receiver)
                        .setTimestamp(ZonedDateTime.now())
        );
    }
}
