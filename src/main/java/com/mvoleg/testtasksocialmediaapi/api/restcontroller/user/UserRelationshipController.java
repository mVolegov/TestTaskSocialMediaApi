package com.mvoleg.testtasksocialmediaapi.api.restcontroller.user;

import com.mvoleg.testtasksocialmediaapi.api.dto.relationship.RelationshipResponseDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.relationship.RelationshipRequestDTO;
import com.mvoleg.testtasksocialmediaapi.exception.ApiError;
import com.mvoleg.testtasksocialmediaapi.service.UsersRelationshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/relations")
@Tag(
        name = "Отношения дружбы и подписки между пользователями",
        description = "Эндпоинты для управления отношениями между пользователями"
)
public class UserRelationshipController {

    private final UsersRelationshipService relationshipService;

    public UserRelationshipController(UsersRelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @PostMapping("/send-request")
    @Operation(
            summary = "Отправить заявку в друзья пользователю (подписаться)",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "RelationshipRequestDTO",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RelationshipRequestDTO.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Заявка успешно отправлена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RelationshipResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь с заданным username не существует",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Между пользователями уже существет подписка",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Пользователь из JWT токена не совпадает с пользователем из тела запроса",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public ResponseEntity<RelationshipResponseDTO> sendRequest(
            @RequestBody RelationshipRequestDTO relationshipRequestDTO) {
        relationshipService.subscribe(relationshipRequestDTO);

        String sender = relationshipRequestDTO.sender();
        String receiver = relationshipRequestDTO.receiver();
        RelationshipResponseDTO relationshipResponseDTO = new RelationshipResponseDTO(
                "Пользователь " + sender + " теперь подписан на пользователя " + receiver
        );

        return new ResponseEntity<>(relationshipResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/withdraw-request")
    @Operation(
            summary = "Отозвать заявку в друзья (отписаться)",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "RelationshipRequestDTO",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RelationshipRequestDTO.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Подписка успешно отменена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RelationshipResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь с заданным username не существует",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Между пользователями не существет подписка",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Пользователь из JWT токена не совпадает с пользователем из тела запроса",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public ResponseEntity<RelationshipResponseDTO> withdrawRequest(
            @RequestBody RelationshipRequestDTO relationshipRequestDTO) {
        relationshipService.unsubscribe(relationshipRequestDTO);

        String sender = relationshipRequestDTO.sender();
        String receiver = relationshipRequestDTO.receiver();
        RelationshipResponseDTO relationshipResponseDTO = new RelationshipResponseDTO(
                "User " + sender + " unfollowed user " + receiver
        );

        return new ResponseEntity<>(relationshipResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/accept-request")
    @Operation(
            summary = "Принять заявку в друзья",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "RelationshipRequestDTO",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RelationshipRequestDTO.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Заявка успешно принята",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RelationshipResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь с заданным username не существует",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Между пользователями не существет подписка " +
                            "/ Отношение дружбы уже существует",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Пользователь из JWT токена не совпадает с пользователем из тела запроса",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public ResponseEntity<RelationshipResponseDTO> acceptRequest(
            @RequestBody RelationshipRequestDTO relationshipRequestDTO) {
        relationshipService.accept(relationshipRequestDTO);

        String sender = relationshipRequestDTO.sender();
        String receiver = relationshipRequestDTO.receiver();
        RelationshipResponseDTO relationshipResponseDTO = new RelationshipResponseDTO(
                "User " + sender + " and user " + receiver + " is now friends"
        );

        return new ResponseEntity<>(relationshipResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/reject-request")
    @Operation(
            summary = "Отклонить заявку в друзья",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "RelationshipRequestDTO",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RelationshipRequestDTO.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Заявка успешно отклонена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RelationshipResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь с заданным username не существует",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пользователи уже являются друзьями " +
                            "/ Между пользователями не существет подписка",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Пользователь из JWT токена не совпадает с пользователем из тела запроса",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public ResponseEntity<RelationshipResponseDTO> rejectRequest(
            @RequestBody RelationshipRequestDTO relationshipRequestDTO) {
        relationshipService.reject(relationshipRequestDTO);

        String sender = relationshipRequestDTO.sender();
        String receiver = relationshipRequestDTO.receiver();
        RelationshipResponseDTO relationshipResponseDTO = new RelationshipResponseDTO(
                "User " + receiver + " rejected a friend request from user " + sender
        );

        return new ResponseEntity<>(relationshipResponseDTO, HttpStatus.OK);
    }
}
