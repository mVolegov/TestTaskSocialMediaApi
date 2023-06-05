package com.mvoleg.testtasksocialmediaapi.api.restcontroller.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvoleg.testtasksocialmediaapi.api.dto.feed.FeedPostDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.post.PostCreateRequestDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.post.PostCreateResponseDTO;
import com.mvoleg.testtasksocialmediaapi.exception.ApiError;
import com.mvoleg.testtasksocialmediaapi.service.FeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feed")
@Tag(
        name = "Лента пользователей",
        description = "Эндпоинты для управления лентой из постов пользователей"
)
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/{userId}")
    @Operation(
            summary = "Получить ленту пользователя (с пагинацией)",
            parameters = {
                    @Parameter(name = "userId", in = ParameterIn.PATH, description = "ID пользователя"),
                    @Parameter(name = "page", in = ParameterIn.QUERY, description = "страница"),
                    @Parameter(name = "limit", in = ParameterIn.QUERY, description = "количество постов на странице"),
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Лента получена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Не совпадает пользователь из JWT токена " +
                            "и пользователь для которого запрашивается лента",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден / У пользователя нет подписок",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public ResponseEntity<Page<FeedPostDTO>> getPostsOfFollowedAuthors(
            @PathVariable("userId") Long userId,
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit
    ) {
        Page<FeedPostDTO> feedPostDTOS = feedService.getPostsOfFollowedAuthorsWithPagination(userId, page, limit);

        return new ResponseEntity<>(feedPostDTOS, HttpStatus.OK);
    }

    @GetMapping("/images/{imageId}")
    @Operation(
            summary = "Получить изображение для поста",
            parameters = @Parameter(name = "imageId", in = ParameterIn.PATH, description = "ID изображения")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Изображение получено",
                    content = @Content(mediaType = "image/png")),
            @ApiResponse(
                    responseCode = "404",
                    description = "Изображение не найдено",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Неверный или истекший токен",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ObjectMapper.class)
                    )
            )
    })
    public ResponseEntity<?> getImage(@PathVariable("imageId") Long imageId) {
        InputStreamResource image = feedService.getImage(imageId);

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }
}
