package com.infrafix.citizen_reporting.repo;

import com.infrafix.citizen_reporting.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByRole(String role);
}
