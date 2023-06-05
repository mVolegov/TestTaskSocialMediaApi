package com.mvoleg.testtasksocialmediaapi.api.restcontroller.user;

import com.mvoleg.testtasksocialmediaapi.api.dto.chat.ChatDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.relationship.RelationshipRequestDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.relationship.RelationshipResponseDTO;
import com.mvoleg.testtasksocialmediaapi.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/chats")
@Tag(
        name = "Чат между пользователями",
        description = "Эндпоинты для управления чатами пользователей"
)
public class UserChatController {

    @GetMapping("/{chatId}")
    @Operation(
            summary = "Получить конкретный чат по его ID",
            parameters = @Parameter(name = "chatId", in = ParameterIn.PATH, description = "ID чата")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Чат успешно получен",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Чата с заданным ID не существует",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public ResponseEntity<?> getById(@PathVariable("chatId") Long chatId) {
        return new ResponseEntity<>(new ChatDTO("Request for chat stub"), HttpStatus.OK);
    }
}
