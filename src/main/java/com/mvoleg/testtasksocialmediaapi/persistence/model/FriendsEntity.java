package com.mvoleg.testtasksocialmediaapi.persistence.model;

import jakarta.persistence.*;

@Entity
@Table(name = "friends")
public class FriendsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "first_user_id", referencedColumnName = "id", nullable = false)
    private UserEntity firstUser;

    @ManyToOne(optional = false)
    @JoinColumn(name = "second_user_id", referencedColumnName = "id", nullable = false)
    private UserEntity secondUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(UserEntity firstUser) {
        this.firstUser = firstUser;
    }

    public UserEntity getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(UserEntity secondUser) {
        this.secondUser = secondUser;
    }
}
