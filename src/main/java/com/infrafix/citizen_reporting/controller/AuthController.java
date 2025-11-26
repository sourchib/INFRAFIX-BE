package com.infrafix.citizen_reporting.controller;

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
}
