package com.mvoleg.testtasksocialmediaapi.service.impl;

import com.mvoleg.testtasksocialmediaapi.api.dto.image.ImageDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.post.FromDTOToEntityPostMapper;
import com.mvoleg.testtasksocialmediaapi.api.dto.post.FromEntityToDTOPostMapper;
import com.mvoleg.testtasksocialmediaapi.api.dto.post.PostCreateRequestDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.post.PostCreateResponseDTO;
import com.mvoleg.testtasksocialmediaapi.exception.post.*;
import com.mvoleg.testtasksocialmediaapi.persistence.model.ImageEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.model.PostEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.model.UserEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.ImageRepository;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.PostRepository;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.UserRepository;
import com.mvoleg.testtasksocialmediaapi.security.SecurityUtils;
import com.mvoleg.testtasksocialmediaapi.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final FromEntityToDTOPostMapper fromEntityToDTOPostMapper;
    private final FromDTOToEntityPostMapper fromDTOToEntityPostMapper;
    private final SecurityUtils.CheckForAuthentication checkForAuthentication;

    public PostServiceImpl(PostRepository postRepository,
                           ImageRepository imageRepository,
                           UserRepository userRepository,
                           FromEntityToDTOPostMapper fromEntityToDTOPostMapper,
                           FromDTOToEntityPostMapper fromDTOToEntityPostMapper,
                           SecurityUtils.CheckForAuthentication checkForAuthentication) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.fromEntityToDTOPostMapper = fromEntityToDTOPostMapper;
        this.fromDTOToEntityPostMapper = fromDTOToEntityPostMapper;
        this.checkForAuthentication = checkForAuthentication;
    }

    @Transactional
    @Override
    public PostCreateResponseDTO create(PostCreateRequestDTO postCreateRequestDTO) {
        String authorUsername = postCreateRequestDTO.authorUsername();

        checkForAuthentication(authorUsername);

        UserEntity authorEntity = userRepository
                .findByUsername(authorUsername)
                .orElseThrow(() -> new AuthorNotFoundException(authorUsername));

        PostEntity postEntity = fromDTOToEntityPostMapper.apply(postCreateRequestDTO);
        postEntity.setAuthor(authorEntity);

        postRepository.save(postEntity);

        return fromEntityToDTOPostMapper.apply(postEntity);
    }

    @Transactional
    @Override
    public List<PostCreateResponseDTO> getByAuthorId(long authorId) {
        UserEntity authorEntity = userRepository
                .findById(authorId)
                .orElseThrow(() -> new UserNotFoundException(authorId));

        checkForAuthentication(authorEntity.getUsername());

        return postRepository
                .findAllByAuthor(authorEntity)
                .orElseThrow(() -> new PostNotFoundException(authorEntity.getUsername()))
                .stream()
                .map(fromEntityToDTOPostMapper)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public PostCreateResponseDTO updateByAuthorId(
            long postId,
            long authorId,
            PostCreateRequestDTO postCreateRequestDTO
    ) {
        UserEntity authorEntity = userRepository
                .findById(authorId)
                .orElseThrow(() -> new UserNotFoundException(authorId));

        checkForAuthentication(authorEntity.getUsername());

        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException(postId);
        }

        PostEntity updatedPostEntity = fromDTOToEntityPostMapper.apply(postCreateRequestDTO);
        updatedPostEntity.setId(postId);
        updatedPostEntity.setAuthor(authorEntity);

        postRepository.save(updatedPostEntity);

        return fromEntityToDTOPostMapper.apply(updatedPostEntity);
    }

    @Transactional
    @Override
    public void deleteByAuthorId(long postId, long authorId) {
        UserEntity authorEntity = userRepository
                .findById(authorId)
                .orElseThrow(() -> new UserNotFoundException(authorId));

        checkForAuthentication(authorEntity.getUsername());

        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException(postId);
        }

        postRepository.deleteByIdAndAuthor(postId, authorEntity);
    }

    @Transactional
    @Override
    public ImageDTO assignImageToPost(long postId, MultipartFile multipartFile) {
        UserEntity postAuthorEntity = postRepository
                .findAuthorByPostId(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        checkForAuthentication(postAuthorEntity.getUsername());

        byte[] imageDataBytes;
        try {
            imageDataBytes = multipartFile.getBytes();
        } catch (Exception ex) {
            throw new ImageProcessingException();
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();
//        byte[] data = imageUtils.compressImage(imageDataBytes);
        byte[] data = imageDataBytes;
        long size = multipartFile.getSize();

        ImageEntity imageEntityToAdd = new ImageEntity();
        imageEntityToAdd.setName(originalFilename);
        imageEntityToAdd.setType(contentType);
        imageEntityToAdd.setImageData(data);
        imageEntityToAdd.setSize(size);

        PostEntity postEntity = postRepository
                .findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        imageEntityToAdd.setPost(postEntity);
        List<ImageEntity> postImages = postEntity.getImages();
        postImages.add(imageEntityToAdd);
        postEntity.setImages(postImages);

        ImageEntity savedImageEntity = imageRepository.save(imageEntityToAdd);

        postRepository.save(postEntity);

        return new ImageDTO(savedImageEntity.getId(), originalFilename, contentType);
    }

    @Transactional
    @Override
    public void deleteImageFromPost(long postId, long imageId) {
        UserEntity postAuthorEntity = postRepository
                .findAuthorByPostId(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        checkForAuthentication(postAuthorEntity.getUsername());

        PostEntity postEntity = postRepository
                .findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        ImageEntity imageEntityToDelete = imageRepository
                .findById(imageId)
                .orElseThrow(() -> new ImageNotFoundException(imageId));

        List<ImageEntity> postImages = postEntity.getImages();
        postImages.remove(imageEntityToDelete);

        postEntity.setImages(postImages);

        imageRepository.delete(imageEntityToDelete);

        postRepository.save(postEntity);
    }

    private void checkForAuthentication(String username) {
        checkForAuthentication.accept(
                username,
                "Пользователь из JWT токена не совпадает с автором поста"
        );
    }
}
