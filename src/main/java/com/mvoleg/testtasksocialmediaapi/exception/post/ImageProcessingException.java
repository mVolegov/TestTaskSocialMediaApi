package com.mvoleg.testtasksocialmediaapi.exception.post;

import com.mvoleg.testtasksocialmediaapi.exception.ApiError;
import com.mvoleg.testtasksocialmediaapi.exception.BaseException;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ImageProcessingException extends BaseException {

    public ImageProcessingException() {
        super(
                HttpStatus.INTERNAL_SERVER_ERROR,
                new ApiError()
                        .setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .setMessage("Возникли некоторые неполадки при обработке изображения")
                        .setTimestamp(ZonedDateTime.now())
        );
    }
}
