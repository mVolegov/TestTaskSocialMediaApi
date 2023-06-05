package com.mvoleg.testtasksocialmediaapi.exception.post;

import com.mvoleg.testtasksocialmediaapi.exception.ApiError;
import com.mvoleg.testtasksocialmediaapi.exception.BaseException;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class AuthorNotFoundException extends BaseException {

    public AuthorNotFoundException(String authorUsername) {
        super(
                HttpStatus.NOT_FOUND,
                new ApiError()
                        .setStatusCode(HttpStatus.NOT_FOUND.value())
                        .setMessage("Автор с именем " + authorUsername + " не найден")
                        .setTimestamp(ZonedDateTime.now())
        );
    }
}
