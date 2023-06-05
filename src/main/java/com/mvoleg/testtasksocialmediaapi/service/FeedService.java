package com.mvoleg.testtasksocialmediaapi.service;

import com.mvoleg.testtasksocialmediaapi.api.dto.feed.FeedDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.feed.FeedPostDTO;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;

import java.util.List;

;

public interface FeedService {

    List<FeedDTO> getPostsOfFollowedAuthors(long userId);

    Page<FeedPostDTO> getPostsOfFollowedAuthorsWithPagination(long userId, int page, int limit);

    InputStreamResource getImage(long imageId);
}
