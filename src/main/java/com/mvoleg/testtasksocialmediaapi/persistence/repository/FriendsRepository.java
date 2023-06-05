package com.mvoleg.testtasksocialmediaapi.persistence.repository;

import com.mvoleg.testtasksocialmediaapi.persistence.model.FriendsEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository<FriendsEntity, Long> {

    @Query("select f from FriendsEntity f where f.firstUser = ?1 or f.secondUser = ?1")
    Optional<FriendsEntity> findByAnyUser(UserEntity user);
}
