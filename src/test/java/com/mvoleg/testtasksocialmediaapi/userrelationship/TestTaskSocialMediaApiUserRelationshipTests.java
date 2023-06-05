package com.mvoleg.testtasksocialmediaapi.userrelationship;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvoleg.testtasksocialmediaapi.api.dto.relationship.RelationshipRequestDTO;
import com.mvoleg.testtasksocialmediaapi.persistence.model.RoleEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.model.Status;
import com.mvoleg.testtasksocialmediaapi.persistence.model.SubscribersEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.model.UserEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.RoleRepository;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.SubscribersRepository;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.UserRepository;
import com.mvoleg.testtasksocialmediaapi.security.SecurityConstants;
import com.mvoleg.testtasksocialmediaapi.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestTaskSocialMediaApiUserRelationshipTests {

    private String baseUrl = "/api/v1/users/relations";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscribersRepository subscribersRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private RoleEntity roleEntity;
    private UserEntity userEntity1;
    private UserEntity userEntity2;
    private RelationshipRequestDTO relationshipRequestDTO;
    private String tokenForUser1;
    private String tokenForUser2;

    @AfterEach
    public void resetDb() {
        subscribersRepository.deleteAll();
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

        tokenForUser1 = jwtTokenProvider.generateToken(userEntity1.getUsername());

        userEntity2 = new UserEntity();
        userEntity2.setUsername("bob");
        userEntity2.setPassword("bob");
        userEntity2.setEmail("bob@pochta.ru");
        userEntity2.setCreatedAt(LocalDateTime.now());
        userEntity2.setUpdatedAt(LocalDateTime.now());
        userEntity2.setStatus(Status.ACTIVE);
        userEntity2.setRoles(Collections.singletonList(roleEntity));
        userRepository.save(userEntity2);

        tokenForUser2 = jwtTokenProvider.generateToken(userEntity2.getUsername());

        relationshipRequestDTO = new RelationshipRequestDTO(
                userEntity1.getUsername(),
                userEntity2.getUsername()
        );
    }

    @Test
    public void givenTwoUsers_whenSubscribe_thenStatus201andRelationshipResponseDTOReturned() throws Exception {
        mockMvc.perform(
                    post(baseUrl + "/send-request")
                            .header("Authorization", "Bearer " + tokenForUser1)
                            .content(objectMapper.writeValueAsString(relationshipRequestDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.message").value(
                                "Пользователь " + userEntity1.getUsername()
                                        + " теперь подписан на пользователя " + userEntity2.getUsername()
                        )
                );
    }

    @Test
    public void givenTwoUsersWithDifferentAuthHeader_whenSubscribe_thenStatus403() throws Exception {
        mockMvc.perform(
                    post(baseUrl + "/send-request")
                            .header("Authorization", "Bearer " + tokenForUser2)
                            .content(objectMapper.writeValueAsString(relationshipRequestDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andExpect(
                        jsonPath("$.message").value(
                                "Пользователь из JWT токена не совпадает с пользователем из тела запроса"
                        )
                );
    }

    @Test
    public void givenTwoSameUsers_whenSubscribe_thenSenderAndReceiverMatchesExceptionReturned() throws Exception {
        RelationshipRequestDTO relationshipRequestDTOWithSameUsers =
                new RelationshipRequestDTO(userEntity1.getUsername(), userEntity1.getUsername());

        mockMvc.perform(
                    post(baseUrl + "/send-request")
                            .header("Authorization", "Bearer " + tokenForUser1)
                            .content(objectMapper.writeValueAsString(relationshipRequestDTOWithSameUsers))
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "Отправитель и получатель заявки совпадают"
                ));
    }

    @Test
    public void givenTwoAlreadySubscribedUsers_whenSubscribe_thenUserAlreadySubscribedException() throws Exception {
        SubscribersEntity subscribersEntity = new SubscribersEntity();
        subscribersEntity.setSender(userEntity1);
        subscribersEntity.setReceiver(userEntity2);
        subscribersRepository.save(subscribersEntity);

        mockMvc.perform(
                    post(baseUrl + "/send-request")
                            .header("Authorization", "Bearer " + tokenForUser1)
                            .content(objectMapper.writeValueAsString(relationshipRequestDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "Пользователь " + userEntity1.getUsername()
                                + " уже подписан на пользователя " + userEntity2.getUsername()
                ));
    }
}
