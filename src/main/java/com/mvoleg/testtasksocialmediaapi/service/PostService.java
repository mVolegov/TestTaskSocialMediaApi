package com.mvoleg.testtasksocialmediaapi.service;

import com.mvoleg.testtasksocialmediaapi.api.dto.image.ImageDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.post.PostCreateRequestDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.post.PostCreateResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    PostCreateResponseDTO create(PostCreateRequestDTO postCreateRequestDTO);

    List<PostCreateResponseDTO> getByAuthorId(long authorId);

    PostCreateResponseDTO updateByAuthorId(long postId, long authorId, PostCreateRequestDTO postCreateRequestDTO);

    void deleteByAuthorId(long postId, long authorId);

    ImageDTO assignImageToPost(long postId, MultipartFile multipartFile);

    void deleteImageFromPost(long postId, long imageId);
}
