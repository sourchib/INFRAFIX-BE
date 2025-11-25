package com.infrafix.citizen_reporting.controller;

import com.infrafix.citizen_reporting.config.OtherConfig;
import com.infrafix.citizen_reporting.model.User;
import com.infrafix.citizen_reporting.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody User user, HttpServletRequest request) {
        return userService.save(user, request);
    }


//    @GetMapping{"/{id}"}
//    public ResponseEntity<Object> findAllUsers(HttpServletRequest request) {
//        Pageable pageable = PageRequest.of(0, OtherConfig.getDefaultPaginationSize(), Sort.by("id"));
//        return userService.findAll(pageable, request);
}
