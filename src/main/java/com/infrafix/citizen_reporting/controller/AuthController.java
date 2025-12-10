package com.infrafix.citizen_reporting.controller;

import com.infrafix.citizen_reporting.dto.response.UserResponseDTO;
import com.infrafix.citizen_reporting.dto.validation.ValUserCreateDTO;
import com.infrafix.citizen_reporting.model.User;
import com.infrafix.citizen_reporting.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User loginRequest, HttpServletRequest request) {
        // delegate login to AuthService
        return authService.login(loginRequest, request);
    }

    /**
     * Register a new user
     *
     * Example JSON request:
     * {
     *   "name": "John Doe",
     *   "email": "john.doe@example.com",
     *   "password": "Password123!",
     *   "phoneNumber": "+6281234567890",
     *   "address": "Jl. Example Street No. 123, Jakarta",
     *   "postCode": "12345"
     * }
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody ValUserCreateDTO userCreateDTO) {
        return authService.register(userCreateDTO);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Object> verifyEmail(@RequestParam String token, HttpServletRequest request) {
        return authService.verifyEmail(token, request);
    }

}
