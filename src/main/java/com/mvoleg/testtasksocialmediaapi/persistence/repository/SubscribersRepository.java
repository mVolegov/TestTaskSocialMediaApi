package com.mvoleg.testtasksocialmediaapi.persistence.repository;

import com.mvoleg.testtasksocialmediaapi.persistence.model.SubscribersEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscribersRepository extends JpaRepository<SubscribersEntity, Long> {

    boolean existsBySenderAndReceiver(UserEntity sender, UserEntity receiver);

    Optional<SubscribersEntity> findBySenderAndReceiver(UserEntity sender, UserEntity receiver);

    Optional<List<SubscribersEntity>> findAllBySender(UserEntity sender);
}
