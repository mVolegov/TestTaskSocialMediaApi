package com.mvoleg.testtasksocialmediaapi.service.impl;

import com.mvoleg.testtasksocialmediaapi.api.dto.relationship.RelationshipRequestDTO;
import com.mvoleg.testtasksocialmediaapi.exception.post.UserNotFoundException;
import com.mvoleg.testtasksocialmediaapi.exception.relationship.*;
import com.mvoleg.testtasksocialmediaapi.persistence.model.SubscribeRequestState;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.FriendsRepository;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.SubscribeStateRepository;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.SubscribersRepository;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.UserRepository;
import com.mvoleg.testtasksocialmediaapi.persistence.model.FriendsEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.model.SubscribersEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.model.UserEntity;
import com.mvoleg.testtasksocialmediaapi.security.SecurityUtils;
import com.mvoleg.testtasksocialmediaapi.service.UsersRelationshipService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRelationshipServiceImpl implements UsersRelationshipService {

    private final UserRepository userRepository;
    private final SubscribersRepository subscribersRepository;
    private final FriendsRepository friendsRepository;
    private final SubscribeStateRepository subscribeStateRepository;
    private final SecurityUtils.CheckForAuthentication checkForAuthentication;

    public UserRelationshipServiceImpl(UserRepository userRepository,
                                       SubscribersRepository subscribersRepository,
                                       FriendsRepository friendsRepository,
                                       SubscribeStateRepository subscribeStateRepository,
                                       SecurityUtils.CheckForAuthentication checkForAuthentication) {
        this.userRepository = userRepository;
        this.subscribersRepository = subscribersRepository;
        this.friendsRepository = friendsRepository;
        this.subscribeStateRepository = subscribeStateRepository;
        this.checkForAuthentication = checkForAuthentication;
    }

    @Transactional
    @Override
    public SubscribersEntity subscribe(RelationshipRequestDTO relationshipRequestDTO) {
        String senderUsername = relationshipRequestDTO.sender();
        UserEntity senderUser = tryGetUserByUsername(senderUsername);

        checkForAuthentication(senderUsername);

        String receiverUsername = relationshipRequestDTO.receiver();
        UserEntity receiverUser = tryGetUserByUsername(receiverUsername);

        if (senderUsername.equals(receiverUsername)) {
            throw new SenderAndReceiverMatchesException();
        }

        if (subscribersRepository.existsBySenderAndReceiver(senderUser, receiverUser)) {
            throw new UserAlreadySubscribedException(senderUsername, receiverUsername);
        }

        SubscribersEntity subscribersEntity = new SubscribersEntity();
        subscribersEntity.setSender(senderUser);
        subscribersEntity.setReceiver(receiverUser);

        // Подписка с обратной стороны уже существует
        if (subscribersRepository.existsBySenderAndReceiver(receiverUser, senderUser)) {
            FriendsEntity friendsEntity = new FriendsEntity();
            friendsEntity.setFirstUser(senderUser);
            friendsEntity.setSecondUser(receiverUser);

            friendsRepository.save(friendsEntity);
        }

        SubscribeRequestState subscribeRequestState = new SubscribeRequestState();
        subscribeRequestState.setSubscribers(subscribersEntity);
        subscribeRequestState.setViewed(false);

        subscribersEntity.setSubscriptionState(subscribeRequestState);

        return subscribersRepository.save(subscribersEntity);
    }

    @Transactional
    @Override
    public void unsubscribe(RelationshipRequestDTO relationshipRequestDTO) {
        String senderUsername = relationshipRequestDTO.sender();
        UserEntity senderUser = tryGetUserByUsername(senderUsername);

        checkForAuthentication(senderUsername);

        String receiverUsername = relationshipRequestDTO.receiver();
        UserEntity receiverUser = tryGetUserByUsername(receiverUsername);

        SubscribersEntity subscription = subscribersRepository
                .findBySenderAndReceiver(senderUser, receiverUser)
                .orElseThrow(
                        () -> new UserNotSubscribedException(senderUsername, receiverUsername)
                );
        subscribersRepository.delete(subscription);

        if (friendsRepository.findByAnyUser(senderUser).isPresent()) {
            FriendsEntity friends = friendsRepository.findByAnyUser(receiverUser).get();
            friendsRepository.delete(friends);
        }
    }

    @Transactional
    @Override
    public void accept(RelationshipRequestDTO relationshipRequestDTO) {
        String senderUsername = relationshipRequestDTO.sender();
        UserEntity senderUser = tryGetUserByUsername(senderUsername);

        checkForAuthentication(senderUsername);

        String receiverUsername = relationshipRequestDTO.receiver();
        UserEntity receiverUser = tryGetUserByUsername(receiverUsername);

        SubscribersEntity subscribersEntityStraight = subscribersRepository
                .findBySenderAndReceiver(senderUser, receiverUser)
                .orElseThrow(
                        () -> new UserNotSubscribedException(senderUsername, receiverUsername)
                );

        SubscribeRequestState subscribeStraightRequestState = subscribeStateRepository
                .findBySubscribers(subscribersEntityStraight)
                .orElseThrow(
                        () -> new UserNotSubscribedException(senderUsername, receiverUsername)
                );
        subscribeStraightRequestState.setViewed(true);

        subscribeStateRepository.save(subscribeStraightRequestState);

        SubscribeRequestState subscribeReversedRequestState = new SubscribeRequestState();
        subscribeReversedRequestState.setViewed(true);

        SubscribersEntity subscribersEntityReversed = new SubscribersEntity();
        subscribersEntityReversed.setReceiver(senderUser);
        subscribersEntityReversed.setSender(receiverUser);
        subscribersEntityReversed.setSubscriptionState(subscribeReversedRequestState);

        subscribersRepository.save(subscribersEntityReversed);

        if (friendsRepository.findByAnyUser(senderUser).isPresent()) {
            throw new FriendRelationshipAlreadyExistsException(senderUsername, receiverUsername);
        }

        FriendsEntity friendsEntity = new FriendsEntity();
        friendsEntity.setFirstUser(senderUser);
        friendsEntity.setSecondUser(receiverUser);

        friendsRepository.save(friendsEntity);
    }

    @Transactional
    @Override
    public void reject(RelationshipRequestDTO relationshipRequestDTO) {
        String senderUsername = relationshipRequestDTO.sender();
        UserEntity senderUser = tryGetUserByUsername(senderUsername);

        checkForAuthentication(senderUsername);

        String receiverUsername = relationshipRequestDTO.receiver();
        UserEntity receiverUser = tryGetUserByUsername(receiverUsername);

        if (!subscribersRepository.existsBySenderAndReceiver(senderUser, receiverUser)) {
            throw new UserNotSubscribedException(senderUsername, receiverUsername);
        }

        if (friendsRepository.findByAnyUser(senderUser).isPresent()) {
            throw new NothingToRejectException(senderUsername, receiverUsername);
        }

        SubscribersEntity subscribersEntity = subscribersRepository
                .findBySenderAndReceiver(senderUser, receiverUser)
                .orElseThrow(
                        () -> new UserNotSubscribedException(senderUsername, receiverUsername)
                );

        SubscribeRequestState subscribeRequestState = subscribeStateRepository
                .findBySubscribers(subscribersEntity)
                .orElseThrow(
                        () -> new UserNotSubscribedException(senderUsername, receiverUsername)
                );
        subscribeRequestState.setViewed(true);

        subscribeStateRepository.save(subscribeRequestState);
    }

    private UserEntity tryGetUserByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    private void checkForAuthentication(String username) {
        checkForAuthentication.accept(
                username,
                "Пользователь из JWT токена не совпадает с пользователем из тела запроса"
        );
    }
}
