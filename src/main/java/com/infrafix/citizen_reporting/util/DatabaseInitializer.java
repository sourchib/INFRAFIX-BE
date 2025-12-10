package com.infrafix.citizen_reporting.util;

import com.infrafix.citizen_reporting.model.Role;
import com.infrafix.citizen_reporting.model.Status;
import com.infrafix.citizen_reporting.model.User;
import com.infrafix.citizen_reporting.repo.RoleRepository;
import com.infrafix.citizen_reporting.repo.StatusRepository;
import com.infrafix.citizen_reporting.repo.UserRepository;
import com.infrafix.citizen_reporting.security.BcryptCustom;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final BcryptCustom bcryptCustom;

    public DatabaseInitializer(RoleRepository roleRepository, StatusRepository statusRepository,
                              UserRepository userRepository, BcryptCustom bcryptCustom) {
        this.roleRepository = roleRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.bcryptCustom = bcryptCustom;
    }

    @Override
    public void run(String... args) throws Exception {
        // Skip initialization if data=true argument is provided
        for (String arg : args) {
            if ("data=true".equals(arg)) {
                System.out.println("Database initialization skipped due to data=true argument.");
                return;
            }
        }
        initializeRoles();
        initializeStatuses();
        // initializeAdmin();
    }

    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            List<Role> roles = Arrays.asList(
                    createRole("citizen"),
                    createRole("admin"),
                    createRole("technician")
            );
            roleRepository.saveAll(roles);
            System.out.println("Default roles initialized.");
        }
    }

    private void initializeStatuses() {
        if (statusRepository.count() == 0) {
            List<Status> statuses = Arrays.asList(
                    createStatus("Pending"),
                    createStatus("In Progress"),
                    createStatus("Completed"),
                    createStatus("Rejected")
            );
            statusRepository.saveAll(statuses);
            System.out.println("Default statuses initialized.");
        }
    }

    private Role createRole(String roleName) {
        Role role = new Role();
        role.setRole(roleName);
        return role;
    }

    // private void initializeAdmin() {
    //     if (userRepository.count() == 0) {
    //         User admin = new User();
    //         admin.setName("Admin");
    //         admin.setEmail("admin@example.com");
    //         admin.setPhoneNumber("08123456789");
    //         admin.setAddress("System");
    //         admin.setPostCode("00000");
    //         admin.setCreatedBy(1L);
    //         admin.setIsEmailVerified(true);
    //         admin.setPassword(bcryptCustom.hash("Admin123!"));
    //         Role role = roleRepository.findByRole("admin")
    //                 .orElseThrow(() -> new RuntimeException("admin Role Not Found"));
    //         admin.setRole(role);

    //         userRepository.save(admin);
    //         System.out.println("Default admin user initialized.");
    //     }
    // }

    private Status createStatus(String name) {
        Status s = new Status();
        s.setStatus(name);
        return s;
    }
}
