package com.mvoleg.testtasksocialmediaapi.api.dto.feed;

import java.time.ZonedDateTime;
import java.util.List;

public record FeedPostDTO(
        String authorUsername,
        long postId,
        String title,
        String text,
        ZonedDateTime dateOfCreation,
        List<Long> imagesIds
) {
}
