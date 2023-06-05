package com.mvoleg.testtasksocialmediaapi.service.impl;

import com.mvoleg.testtasksocialmediaapi.api.dto.feed.FeedDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.feed.FeedPostDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.feed.FromPostEntityToFeedPostDTOMapper;
import com.mvoleg.testtasksocialmediaapi.api.dto.feed.ImageFeedPostDTO;
import com.mvoleg.testtasksocialmediaapi.exception.post.AuthorNotFoundException;
import com.mvoleg.testtasksocialmediaapi.exception.post.ImageNotFoundException;
import com.mvoleg.testtasksocialmediaapi.exception.post.UserNotFoundException;
import com.mvoleg.testtasksocialmediaapi.exception.relationship.UserHasNoSubscriptionsException;
import com.mvoleg.testtasksocialmediaapi.persistence.model.ImageEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.model.PostEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.model.SubscribersEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.model.UserEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.ImageRepository;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.PostRepository;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.SubscribersRepository;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.UserRepository;
import com.mvoleg.testtasksocialmediaapi.security.SecurityUtils;
import com.mvoleg.testtasksocialmediaapi.service.FeedService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedServiceImpl implements FeedService {

    private final SubscribersRepository subscribersRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PostRepository postRepository;
    private final FromPostEntityToFeedPostDTOMapper fromPostEntityToFeedPostDTOMapper;
    private final SecurityUtils.CheckForAuthentication checkForAuthentication;

    public FeedServiceImpl(SubscribersRepository subscribersRepository,
                           UserRepository userRepository,
                           ImageRepository imageRepository,
                           PostRepository postRepository,
                           FromPostEntityToFeedPostDTOMapper fromPostEntityToFeedPostDTOMapper,
                           SecurityUtils.CheckForAuthentication checkForAuthentication) {
        this.subscribersRepository = subscribersRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.postRepository = postRepository;
        this.fromPostEntityToFeedPostDTOMapper = fromPostEntityToFeedPostDTOMapper;
        this.checkForAuthentication = checkForAuthentication;
    }

    @Transactional
    @Override
    public List<FeedDTO> getPostsOfFollowedAuthors(long userId) {
        List<FeedDTO> feedDTOs = new ArrayList<>();

        UserEntity userEntity = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<SubscribersEntity> subscribersEntities = subscribersRepository
                .findAllBySender(userEntity)
                .orElseGet(ArrayList::new);

        if (subscribersEntities.isEmpty()) {
            throw new UserHasNoSubscriptionsException(userEntity.getUsername());
        }

        for (SubscribersEntity subscribersEntity : subscribersEntities) {
            UserEntity author = subscribersEntity.getReceiver();

            List<PostEntity> postEntities = postRepository
                    .findAllByAuthor(author)
                    .orElseThrow(() -> new AuthorNotFoundException(author.getUsername()));

            List<FeedPostDTO> feedPostDTOs = postEntities
                    .stream()
                    .map(fromPostEntityToFeedPostDTOMapper)
                    .collect(Collectors.toList());

            FeedDTO feedDTO = new FeedDTO(author.getUsername(), feedPostDTOs);
            feedDTOs.add(feedDTO);
        }

        return feedDTOs;
    }

    @Transactional
    @Override
    public Page<FeedPostDTO> getPostsOfFollowedAuthorsWithPagination(long userId, int page, int limit) {
        UserEntity userEntity = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        checkForAuthentication.accept(
                userEntity.getUsername(),
                "Отказано в доступе (не совпадает пользователь из JWT токена " +
                        "и пользователь для которого запрашивается лента)"
        );

        List<SubscribersEntity> subscribersEntities = subscribersRepository
                .findAllBySender(userEntity)
                .orElseGet(ArrayList::new);

        if (subscribersEntities.isEmpty()) {
            throw new UserHasNoSubscriptionsException(userEntity.getUsername());
        }

        List<UserEntity> authors = subscribersEntities
                .stream()
                .map(user -> user.getReceiver())
                .collect(Collectors.toList());

        Page<FeedPostDTO> feedPostDTOs = postRepository
                .findAllByAuthors(
                        authors,
                        PageRequest.of(page, limit).withSort(Sort.by("dateOfCreation"))
                )
                .map(post -> new FeedPostDTO(
                        post.getAuthor().getUsername(),
                        post.getId(),
                        post.getHeader(),
                        post.getText(),
                        post.getDateOfCreation(),
                        post.getImages().stream().map(ImageEntity::getId).toList()
                ));

        return feedPostDTOs;
    }

    @Override
    public InputStreamResource getImage(long imageId) {
        ImageEntity imageEntity = imageRepository
                .findById(imageId)
                .orElseThrow(() -> new ImageNotFoundException(imageId));

        return new InputStreamResource(new ByteArrayInputStream(imageEntity.getImageData()));
    }
}
