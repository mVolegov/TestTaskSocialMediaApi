package com.mvoleg.testtasksocialmediaapi.exception.relationship;

import com.mvoleg.testtasksocialmediaapi.exception.ApiError;
import com.mvoleg.testtasksocialmediaapi.exception.BaseException;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class SenderAndReceiverMatchesException extends BaseException {

    public SenderAndReceiverMatchesException() {
        super(
                HttpStatus.BAD_REQUEST,
                new ApiError()
                        .setStatusCode(HttpStatus.BAD_REQUEST.value())
                        .setMessage("Отправитель и получатель заявки совпадают")
                        .setTimestamp(ZonedDateTime.now())
        );
    }
}
