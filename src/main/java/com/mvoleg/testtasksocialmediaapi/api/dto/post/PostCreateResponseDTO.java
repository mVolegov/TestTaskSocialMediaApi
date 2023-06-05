package com.mvoleg.testtasksocialmediaapi.api.dto.post;

import java.time.ZonedDateTime;

public record PostCreateResponseDTO(
        String authorUsername,
        String title,
        String text,
        ZonedDateTime dateOfCreation
) {
}
