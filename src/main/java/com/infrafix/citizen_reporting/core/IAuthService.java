package com.infrafix.citizen_reporting.core;

import com.infrafix.citizen_reporting.dto.response.UserResponseDTO;
import com.infrafix.citizen_reporting.dto.validation.ValUserCreateDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IAuthService<REQ> extends UserDetailsService {

    ResponseEntity<Object> login(REQ req, HttpServletRequest request);
    ResponseEntity<UserResponseDTO> register(ValUserCreateDTO req);

}
