package com.mvoleg.testtasksocialmediaapi.exception.relationship;

import com.mvoleg.testtasksocialmediaapi.exception.ApiError;
import com.mvoleg.testtasksocialmediaapi.exception.BaseException;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class UserHasNoSubscriptionsException extends BaseException {

    public UserHasNoSubscriptionsException(String username) {
        super(
                HttpStatus.NOT_FOUND,
                new ApiError()
                        .setStatusCode(HttpStatus.NOT_FOUND.value())
                        .setMessage("Пользователь с именем " + username + " не имеет подписок")
                        .setTimestamp(ZonedDateTime.now())
        );
    }
}
