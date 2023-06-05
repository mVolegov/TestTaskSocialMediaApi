package com.mvoleg.testtasksocialmediaapi.api.restcontroller.post;

import com.mvoleg.testtasksocialmediaapi.api.dto.image.ImageDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.post.PostCreateRequestDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.post.PostCreateResponseDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.post.PostResponseDTO;
import com.mvoleg.testtasksocialmediaapi.exception.ApiError;
import com.mvoleg.testtasksocialmediaapi.service.PostService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@Tag(
        name = "Посты пользователей",
        description = "Эндпоинты для управления постами пользователей"
)
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @Operation(
            summary = "Создать пост",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "PostCreateRequestDTO",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostCreateRequestDTO.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Пост успешно создан",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Автор(пользователь) с заданным username не существует",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Пользователь из JWT токена не совпадает с автором поста",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostCreateRequestDTO postCreateRequestDTO) {
        postService.create(postCreateRequestDTO);

        PostResponseDTO postResponseDTO = new PostResponseDTO("Post successfully created");

        return new ResponseEntity<>(postResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/author/{authorId}")
    @Operation(
            summary = "Получить посты определенного автора",
            parameters = @Parameter(name = "authorId", in = ParameterIn.PATH, description = "ID автора")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пост получен",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostCreateResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Автор(пользователь) с заданным username не существует / Пост не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Пользователь из JWT токена не совпадает с автором поста",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public ResponseEntity<List<PostCreateResponseDTO>> getAllAuthorsPosts(@PathVariable("authorId") Long authorId) {
        List<PostCreateResponseDTO> allAuthorsPostsDTOs = postService.getByAuthorId(authorId);

        return new ResponseEntity<>(allAuthorsPostsDTOs, HttpStatus.OK);
    }

    @DeleteMapping("/{authorId}/{postId}")
    @Operation(
            summary = "Удалить определенный пост определенного автора",
            parameters = {
                    @Parameter(name = "authorId", in = ParameterIn.PATH, description = "ID автора"),
                    @Parameter(name = "postId", in = ParameterIn.PATH, description = "ID поста")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пост удален",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostCreateResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Автор(пользователь) с заданным username не существует / Пост не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Пользователь из JWT токена не совпадает с автором поста",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public ResponseEntity<?> deletePost(@PathVariable("authorId") Long authorId, @PathVariable("postId") Long postId) {
        postService.deleteByAuthorId(postId, authorId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{authorId}/{postId}")
    @Operation(
            summary = "Обновить определенный пост определенного автора",
            parameters = {
                    @Parameter(name = "authorId", in = ParameterIn.PATH, description = "ID автора"),
                    @Parameter(name = "postId", in = ParameterIn.PATH, description = "ID поста")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "PostCreateRequestDTO",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostCreateRequestDTO.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пост обновлен",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostCreateResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Автор(пользователь) с заданным username не существует / Пост не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Пользователь из JWT токена не совпадает с автором поста",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public ResponseEntity<PostCreateResponseDTO> updatePost(
            @PathVariable("authorId") Long authorId,
            @PathVariable("postId") Long postId,
            @RequestBody PostCreateRequestDTO postCreateRequestDTO
    ) {
        PostCreateResponseDTO updatedPostDTO = postService.updateByAuthorId(postId, authorId, postCreateRequestDTO);

        return new ResponseEntity<>(updatedPostDTO, HttpStatus.OK);
    }

    @PostMapping("/{postId}/image")
    @Operation(
            summary = "Добавить изображение к посту",
            parameters = {
                    @Parameter(name = "postId", in = ParameterIn.PATH, description = "ID поста"),
                    @Parameter(name = "image",
                            description = "Изображение",
                            schema = @Schema(type = "string", format = "binary")
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Изображение добавлено",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ImageDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Во время добавления изображения возникла ошибка",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пост не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Пользователь из JWT токена не совпадает с автором поста",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public ResponseEntity<ImageDTO> addPostImage(
            @PathVariable("postId") Long postId,
            @RequestParam("image") MultipartFile file
    ) {
        ImageDTO imageDTO = postService.assignImageToPost(postId, file);

        return new ResponseEntity<>(imageDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/image/{imageId}")
    @Operation(
            summary = "Удалить изображение из поста",
            parameters = {
                    @Parameter(name = "postId", in = ParameterIn.PATH, description = "ID поста"),
                    @Parameter(name = "imageId", in = ParameterIn.PATH, description = "ID изображения")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Изображение удалено"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пост не найден / Изображение не найдено",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Пользователь из JWT токена не совпадает с автором поста",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public ResponseEntity<?> deletePostImage(
            @PathVariable("postId") Long postId,
            @PathVariable("imageId") Long imageId
    ) {
        postService.deleteImageFromPost(postId, imageId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
