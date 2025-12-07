package com.infrafix.citizen_reporting.controller;

import com.infrafix.citizen_reporting.model.Role;
import com.infrafix.citizen_reporting.model.User;
import com.infrafix.citizen_reporting.repo.RoleRepository;
import com.infrafix.citizen_reporting.repo.UserRepository;
import com.infrafix.citizen_reporting.security.BcryptCustom;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminInitController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BcryptCustom bcryptCustom;

    public AdminInitController(UserRepository userRepository, RoleRepository roleRepository, BcryptCustom bcryptCustom) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bcryptCustom = bcryptCustom;
    }

    @PostMapping("/init")
    public String initAdmin() {
        if (userRepository.existsByEmail("admin@example.com")) {
        return "Admin already initialized.";
    }

    User admin = new User();
    admin.setName("Admin");
    admin.setEmail("admin@example.com");
    admin.setPhoneNumber("08123456789");
    admin.setAddress("System");
    admin.setPostCode("00000");
    admin.setCreatedBy(1L);
    admin.setPassword(bcryptCustom.hash("Admin123!"));
    Role role = roleRepository.findById(2L)
            .orElseThrow(() -> new RuntimeException("Role Not Found"));
    admin.setRole(role);

    userRepository.save(admin);
    return "Admin Created";
    }


}

