package com.mvoleg.testtasksocialmediaapi.persistence.repository;

import com.mvoleg.testtasksocialmediaapi.persistence.model.PostEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    Optional<List<PostEntity>> findAllByAuthor(UserEntity author);

    @Query("select p from PostEntity p where p.author in ?1")
    Page<PostEntity> findAllByAuthors(List<UserEntity> authors, Pageable pageable);

    @Query("select p.author from PostEntity p where p.id = ?1")
    Optional<UserEntity> findAuthorByPostId(long postId);

    void deleteByIdAndAuthor(long postId, UserEntity author);
}
