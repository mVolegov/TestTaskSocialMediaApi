package com.mvoleg.testtasksocialmediaapi.feed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvoleg.testtasksocialmediaapi.persistence.model.*;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.PostRepository;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.RoleRepository;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.SubscribersRepository;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.UserRepository;
import com.mvoleg.testtasksocialmediaapi.security.SecurityConstants;
import com.mvoleg.testtasksocialmediaapi.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestTaskSocialMediaApiFeedTests {

    private String baseUrl = "/api/v1/feed";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private SubscribersRepository subscribersRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private RoleEntity roleEntity;
    private UserEntity userEntity1;
    private UserEntity userEntity2;
    private String tokenForUser;

    @AfterEach
    public void resetDb() {
        subscribersRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Transactional
    @BeforeEach
    public void setUp() {
        roleEntity = new RoleEntity();
        roleEntity.setName(SecurityConstants.ROLE_APP_USER);
        roleRepository.save(roleEntity);

        userEntity1 = new UserEntity();
        userEntity1.setUsername("alex");
        userEntity1.setPassword("alex");
        userEntity1.setEmail("alex@pochta.ru");
        userEntity1.setCreatedAt(LocalDateTime.now());
        userEntity1.setUpdatedAt(LocalDateTime.now());
        userEntity1.setStatus(Status.ACTIVE);
        userEntity1.setRoles(Collections.singletonList(roleEntity));
        userRepository.save(userEntity1);

        tokenForUser = jwtTokenProvider.generateToken(userEntity1.getUsername());

        userEntity2 = new UserEntity();
        userEntity2.setUsername("bob");
        userEntity2.setPassword("bob");
        userEntity2.setEmail("bob@pochta.ru");
        userEntity2.setCreatedAt(LocalDateTime.now());
        userEntity2.setUpdatedAt(LocalDateTime.now());
        userEntity2.setStatus(Status.ACTIVE);
        userEntity2.setRoles(Collections.singletonList(roleEntity));
        userRepository.save(userEntity2);


    }

    @Test
    public void givenUser_whenGetFeed_thenReturnPageOfFeedPostDTOs() throws Exception {
        SubscribersEntity subscribersEntity = new SubscribersEntity();
        subscribersEntity.setSender(userEntity1);
        subscribersEntity.setReceiver(userEntity2);
        subscribersRepository.save(subscribersEntity);

        PostEntity postEntity = new PostEntity();
        postEntity.setHeader("Заголовок");
        postEntity.setText("Текст");
        postEntity.setDateOfCreation(ZonedDateTime.now());
        postEntity.setAuthor(userEntity2);
        postRepository.save(postEntity);

        mockMvc.perform(
                    get(baseUrl + "/{userId}?page=0&limit=1", userEntity1.getId())
                            .header("Authorization", "Bearer " + tokenForUser)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void givenUserWithNoSubscriptions_whenGetFeed_thenUserHasNoSubscriptionsException() throws Exception {
        mockMvc.perform(
                        get(baseUrl + "/{userId}?page=0&limit=1", userEntity1.getId())
                                .header("Authorization", "Bearer " + tokenForUser)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        "Пользователь с именем " + userEntity1.getUsername() + " не имеет подписок"
                ));
    }
}
