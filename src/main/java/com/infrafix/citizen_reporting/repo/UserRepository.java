package com.infrafix.citizen_reporting.repo;

import com.infrafix.citizen_reporting.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByNameContainsIgnoreCase(Pageable page, String value);
    Page<User> findByAddressContainsIgnoreCase(Pageable page, String value);
    Page<User> findByPostCodeContainsIgnoreCase(Pageable page, String value);

}
