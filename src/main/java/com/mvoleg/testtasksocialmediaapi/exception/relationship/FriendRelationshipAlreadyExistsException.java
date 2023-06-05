package com.mvoleg.testtasksocialmediaapi.exception.relationship;

import com.mvoleg.testtasksocialmediaapi.exception.ApiError;
import com.mvoleg.testtasksocialmediaapi.exception.BaseException;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class FriendRelationshipAlreadyExistsException extends BaseException {

    public FriendRelationshipAlreadyExistsException(String sender, String receiver) {
        super(
                HttpStatus.BAD_REQUEST,
                new ApiError()
                        .setStatusCode(HttpStatus.BAD_REQUEST.value())
                        .setMessage("Пользователь " + sender + " и пользователь " + receiver + " уже являются друзьями")
                        .setTimestamp(ZonedDateTime.now())
        );
    }
}
