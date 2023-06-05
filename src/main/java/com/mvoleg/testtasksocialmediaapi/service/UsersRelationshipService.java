package com.mvoleg.testtasksocialmediaapi.service;

import com.mvoleg.testtasksocialmediaapi.api.dto.relationship.RelationshipRequestDTO;
import com.mvoleg.testtasksocialmediaapi.persistence.model.SubscribersEntity;

public interface UsersRelationshipService {

    SubscribersEntity subscribe(RelationshipRequestDTO relationshipRequestDTO);

    void unsubscribe(RelationshipRequestDTO relationshipRequestDTO);

    void accept(RelationshipRequestDTO relationshipRequestDTO);

    void reject(RelationshipRequestDTO relationshipRequestDTO);
}
