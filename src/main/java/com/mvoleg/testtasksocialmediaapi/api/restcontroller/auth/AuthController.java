package com.mvoleg.testtasksocialmediaapi.api.restcontroller.auth;

import com.mvoleg.testtasksocialmediaapi.api.dto.auth.AuthResponseDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.auth.LoginDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.auth.RegisterDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.auth.RegisterResponseDTO;
import com.mvoleg.testtasksocialmediaapi.exception.ApiError;
import com.mvoleg.testtasksocialmediaapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(
        name = "Авторизация, аутентификация пользователей",
        description = "Эндпоинты для работы с авторизацией и аутентификацией пользователей"
)
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Зарегестрировать нового пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "RegisterDTO",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterDTO.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Пользователь зарегистрирован",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пользователь с заданным email уже существует",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO);
        RegisterResponseDTO registerResponseDTO =
                new RegisterResponseDTO(
                        "Пользователь с именем: "
                        + registerDTO.username()
                        + " успешно зарегистрирован"
                );
        return new ResponseEntity<>(registerResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Выполнить вход (присвоить JWT токен пользователю)",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "LoginDTO",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginDTO.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь успешно получил JWT токен",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь с заданным username не существует",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        AuthResponseDTO authResponseDTO = userService.login(loginDTO);
        return new ResponseEntity<>(authResponseDTO, HttpStatus.OK);
    }
}
