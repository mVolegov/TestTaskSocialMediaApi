package com.mvoleg.testtasksocialmediaapi.api.dto.post;

public record PostCreateRequestDTO(
        String authorUsername,
        String title,
        String text
) {
}
