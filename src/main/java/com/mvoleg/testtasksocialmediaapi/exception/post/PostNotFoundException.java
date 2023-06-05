package com.mvoleg.testtasksocialmediaapi.exception.post;

import com.mvoleg.testtasksocialmediaapi.exception.ApiError;
import com.mvoleg.testtasksocialmediaapi.exception.BaseException;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class PostNotFoundException extends BaseException {

    public PostNotFoundException(Long id) {
        super(
                HttpStatus.NOT_FOUND,
                new ApiError()
                        .setStatusCode(HttpStatus.NOT_FOUND.value())
                        .setMessage("Пост с id " + id + " не найден")
                        .setTimestamp(ZonedDateTime.now())
        );
    }

    public PostNotFoundException(String authorUsername) {
        super(
                HttpStatus.NOT_FOUND,
                new ApiError()
                        .setStatusCode(HttpStatus.NOT_FOUND.value())
                        .setMessage("Пост с автором " + authorUsername + " не найден")
                        .setTimestamp(ZonedDateTime.now())
        );
    }
}
