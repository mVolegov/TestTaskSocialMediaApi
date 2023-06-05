package com.mvoleg.testtasksocialmediaapi.api.dto.post;

import com.mvoleg.testtasksocialmediaapi.persistence.model.PostEntity;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.function.Function;

@Component
public class FromDTOToEntityPostMapper implements Function<PostCreateRequestDTO, PostEntity> {

    @Override
    public PostEntity apply(PostCreateRequestDTO postCreateRequestDTO) {
        PostEntity postEntity = new PostEntity();
        postEntity.setDateOfCreation(ZonedDateTime.now());
        postEntity.setText(postCreateRequestDTO.text());
        postEntity.setHeader(postCreateRequestDTO.title());

        return postEntity;
    }
}
