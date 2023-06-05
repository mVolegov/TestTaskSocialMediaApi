package com.mvoleg.testtasksocialmediaapi.api.dto.post;

import com.mvoleg.testtasksocialmediaapi.persistence.model.PostEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class FromEntityToDTOPostMapper implements Function<PostEntity, PostCreateResponseDTO> {

    @Override
    public PostCreateResponseDTO apply(PostEntity postEntity) {
        return new PostCreateResponseDTO(
                postEntity.getAuthor().getUsername(),
                postEntity.getHeader(),
                postEntity.getText(),
                postEntity.getDateOfCreation()
        );
    }
}
