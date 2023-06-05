package com.mvoleg.testtasksocialmediaapi.exception.post;

import com.mvoleg.testtasksocialmediaapi.exception.ApiError;
import com.mvoleg.testtasksocialmediaapi.exception.BaseException;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ImageNotFoundException extends BaseException {

    public ImageNotFoundException(long imageId) {
        super(
                HttpStatus.NOT_FOUND,
                new ApiError()
                        .setStatusCode(HttpStatus.NOT_FOUND.value())
                        .setMessage("Изображение с id " + imageId + " не найдено")
                        .setTimestamp(ZonedDateTime.now())
        );
    }
}
