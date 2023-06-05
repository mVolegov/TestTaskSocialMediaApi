package com.mvoleg.testtasksocialmediaapi.api.dto.auth;

public record AuthResponseDTO(
        String accessToken,
        String tokenType
) {
    public AuthResponseDTO(String accessToken) {
        this(accessToken, "Bearer");
    }
}
