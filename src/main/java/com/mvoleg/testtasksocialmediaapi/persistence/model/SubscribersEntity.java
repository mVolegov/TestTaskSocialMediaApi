package com.mvoleg.testtasksocialmediaapi.persistence.model;

import jakarta.persistence.*;

@Entity
@Table(name = "subscribers")
public class SubscribersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_user_id", referencedColumnName = "id", nullable = false)
    private UserEntity sender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_user_id", referencedColumnName = "id", nullable = false)
    private UserEntity receiver;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subscription_state_id", referencedColumnName = "id")
    private SubscribeRequestState subscriptionState;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public UserEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    public SubscribeRequestState getSubscriptionState() {
        return subscriptionState;
    }

    public void setSubscriptionState(SubscribeRequestState subscriptionState) {
        this.subscriptionState = subscriptionState;
    }
}
