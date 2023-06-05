package com.mvoleg.testtasksocialmediaapi.api.dto.feed;

import com.mvoleg.testtasksocialmediaapi.persistence.model.ImageEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.model.PostEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FromPostEntityToFeedPostDTOMapper implements Function<PostEntity, FeedPostDTO> {

    @Override
    public FeedPostDTO apply(PostEntity postEntity) {
        return new FeedPostDTO(
                postEntity.getAuthor().getUsername(),
                postEntity.getId(),
                postEntity.getHeader(),
                postEntity.getText(),
                postEntity.getDateOfCreation(),
                postEntity.getImages()
                        .stream()
                        .map(ImageEntity::getId)
                        .collect(Collectors.toList())
        );
    }
}
