package com.mvoleg.testtasksocialmediaapi.service;

import com.mvoleg.testtasksocialmediaapi.api.dto.auth.AuthResponseDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.auth.LoginDTO;
import com.mvoleg.testtasksocialmediaapi.api.dto.auth.RegisterDTO;
import com.mvoleg.testtasksocialmediaapi.persistence.model.UserEntity;

public interface UserService {

    UserEntity register(RegisterDTO registerDTO);

    AuthResponseDTO login(LoginDTO loginDTO);
}
