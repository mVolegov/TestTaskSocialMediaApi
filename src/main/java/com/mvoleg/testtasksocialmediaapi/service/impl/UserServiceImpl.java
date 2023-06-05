package com.mvoleg.testtasksocialmediaapi.service.impl;

import com.mvoleg.testtasksocialmediaapi.api.dto.auth.AuthResponseDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.auth.LoginDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.auth.RegisterDTO;
import com.mvoleg.testtasksocialmediaapi.exception.auth.EmailIsTakenException;
import com.mvoleg.testtasksocialmediaapi.exception.post.UserNotFoundException;
import com.mvoleg.testtasksocialmediaapi.exception.auth.UsernameIsTakenException;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.RoleRepository;
import com.mvoleg.testtasksocialmediaapi.persistence.model.RoleEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.model.Status;
import com.mvoleg.testtasksocialmediaapi.persistence.model.UserEntity;
import com.mvoleg.testtasksocialmediaapi.persistence.repository.UserRepository;
import com.mvoleg.testtasksocialmediaapi.security.SecurityConstants;
import com.mvoleg.testtasksocialmediaapi.security.jwt.JwtTokenProvider;
import com.mvoleg.testtasksocialmediaapi.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    @Override
    public UserEntity register(RegisterDTO registerDTO) {
        if (userRepository.existsByUsername(registerDTO.username())) {
            throw new UsernameIsTakenException(registerDTO.username());
        }

        if (userRepository.existsByEmail(registerDTO.email())) {
            throw new EmailIsTakenException(registerDTO.email());
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registerDTO.username());
        userEntity.setEmail(registerDTO.email());
        userEntity.setPassword(passwordEncoder.encode(registerDTO.password()));
        userEntity.setStatus(Status.ACTIVE);
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setUpdatedAt(LocalDateTime.now());

        RoleEntity roleEntity = roleRepository.findByName(SecurityConstants.ROLE_APP_USER).get();
        userEntity.setRoles(Collections.singletonList(roleEntity));

        return userRepository.save(userEntity);
    }

    @Override
    public AuthResponseDTO login(LoginDTO loginDTO) {
        String username = loginDTO.username();
        String password = loginDTO.password();

        if (!userRepository.existsByUsername(username)) {
            throw new UserNotFoundException(username);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(username);

        return new AuthResponseDTO(token);
    }
}
