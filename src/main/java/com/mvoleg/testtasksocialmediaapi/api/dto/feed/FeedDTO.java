package com.mvoleg.testtasksocialmediaapi.api.dto.feed;

import java.util.List;

public record FeedDTO(
        String authorUsername,
        List<FeedPostDTO> feedPostDTOs
) {
}
