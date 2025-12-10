package com.infrafix.citizen_reporting.repo;

import com.infrafix.citizen_reporting.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // For authentication — exact match
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // Check for duplicate name and address
    // Removed to allow duplicates

    // For searching
    Page<User> findByNameContainsIgnoreCase(Pageable page, String value);

    Page<User> findByAddressContainsIgnoreCase(Pageable page, String value);

    Page<User> findByPostCodeContainsIgnoreCase(Pageable page, String value);

    Page<User> findByEmailContainsIgnoreCase(Pageable page, String value);
}
