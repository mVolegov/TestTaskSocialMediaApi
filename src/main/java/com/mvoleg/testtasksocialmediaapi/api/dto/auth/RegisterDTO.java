package com.mvoleg.testtasksocialmediaapi.api.dto.auth;

public record RegisterDTO(
        String username,
        String email,
        String password
) {
}
