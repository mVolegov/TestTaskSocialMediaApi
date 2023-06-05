package com.mvoleg.testtasksocialmediaapi.persistence.repository;

import com.mvoleg.testtasksocialmediaapi.persistence.model.SubscribeRequestState;
import com.mvoleg.testtasksocialmediaapi.persistence.model.SubscribersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscribeStateRepository extends JpaRepository<SubscribeRequestState, Long> {

    Optional<SubscribeRequestState> findBySubscribers(SubscribersEntity subscribersEntity);
}
