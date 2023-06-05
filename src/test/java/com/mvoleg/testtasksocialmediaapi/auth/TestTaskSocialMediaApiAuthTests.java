package com.mvoleg.testtasksocialmediaapi.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvoleg.testtasksocialmediaapi.api.dto.auth.RegisterDTO;
import com.mvoleg.testtasksocialmediaapi.persistence.model.RoleEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.model.Status;
import com.mvoleg.testtasksocialmediaapi.persistence.model.UserEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.RoleRepository;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.UserRepository;
import com.mvoleg.testtasksocialmediaapi.security.SecurityConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestTaskSocialMediaApiAuthTests {

    private String baseUrl = "/api/v1/auth";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    private RoleEntity roleEntity;
    private UserEntity userEntity1;
    private RegisterDTO registerDTO;

    @AfterEach
    public void resetDb() {
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

        registerDTO = new RegisterDTO(
                userEntity1.getUsername(),
                userEntity1.getEmail(),
                userEntity1.getPassword()
        );
    }

    @Test
    public void givenUser_whenRegister_thenRegisterResponseDTOReturned() throws Exception {
        mockMvc.perform(
                    post(baseUrl + "/register")
                            .content(objectMapper.writeValueAsString(registerDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.message").value(
                                "Пользователь с именем: "
                                        + userEntity1.getUsername()
                                        + " успешно зарегистрирован"
                        )
                );
    }

    @Test
    public void givenUserWithExistingUsername_whenRegister_thenUsernameIsTakenException() throws Exception {
        userRepository.save(userEntity1);

        mockMvc.perform(
                    post(baseUrl + "/register")
                            .content(objectMapper.writeValueAsString(registerDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.message").value(
                                "Имя пользователя " + userEntity1.getUsername() + " уже занято"
                        )
                );
    }
}
