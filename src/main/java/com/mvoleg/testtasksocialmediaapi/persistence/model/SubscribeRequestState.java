package com.mvoleg.testtasksocialmediaapi.persistence.model;

import jakarta.persistence.*;

@Entity
@Table(name = "subscribre_request_state")
public class SubscribeRequestState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "subscriptionState")
    private SubscribersEntity subscribers;

    @Column(name = "is_viewed", nullable = false)
    private boolean isViewed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubscribersEntity getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(SubscribersEntity subscribers) {
        this.subscribers = subscribers;
    }

    public boolean isViewed() {
        return isViewed;
    }

    public void setViewed(boolean viewed) {
        isViewed = viewed;
    }
}
