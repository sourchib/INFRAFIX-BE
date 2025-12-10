package com.infrafix.citizen_reporting.controller;

import com.infrafix.citizen_reporting.dto.validation.ValUserCreateDTO;
import com.infrafix.citizen_reporting.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adminuser")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-technician")

    public ResponseEntity<Object> createTechnician(
            @RequestBody ValUserCreateDTO dto,
            HttpServletRequest request) {
        return userService.createTechnician(dto, request);
    }

    @PostMapping("/create-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> createAdmin(
            @RequestBody ValUserCreateDTO dto,
            HttpServletRequest request) {
        return userService.createAdmin(dto, request);
    }
}
