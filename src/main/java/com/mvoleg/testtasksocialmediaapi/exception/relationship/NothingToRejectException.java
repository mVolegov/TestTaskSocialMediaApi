package com.mvoleg.testtasksocialmediaapi.exception.relationship;

import com.mvoleg.testtasksocialmediaapi.exception.ApiError;
import com.mvoleg.testtasksocialmediaapi.exception.BaseException;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class NothingToRejectException extends BaseException {

    public NothingToRejectException(String sender, String receiver) {
        super(
                HttpStatus.BAD_REQUEST,
                new ApiError()
                        .setStatusCode(HttpStatus.BAD_REQUEST.value())
                        .setMessage("Пользователи " + sender +
                                " и " + receiver +
                                " уже являются друзьями, нечего отклонять")
                        .setTimestamp(ZonedDateTime.now())
        );
    }
}
