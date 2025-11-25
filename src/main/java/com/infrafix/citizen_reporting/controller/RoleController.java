package com.infrafix.citizen_reporting.controller;

import com.infrafix.citizen_reporting.model.Role;
import com.infrafix.citizen_reporting.repo.RoleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostMapping("/init")
    public String initializeRoles() {
        // Check if roles already exist
        if (roleRepository.count() > 0) {
            return "Roles are already initialized.";
        }

        // Create default roles
        List<Role> roles = Arrays.asList(
                createRole("citizen"),
                createRole("admin"),
                createRole("technician")
        );

        roleRepository.saveAll(roles);

        return "Default roles created successfully.";
    }

    // Helper method to create Role objects
    private Role createRole(String roleName) {
        Role role = new Role();
        role.setRole(roleName);
        return role;
    }
}
