package com.infrafix.citizen_reporting.controller;

import com.infrafix.citizen_reporting.config.OtherConfig;
import com.infrafix.citizen_reporting.model.User;
import com.infrafix.citizen_reporting.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


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

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody User user, HttpServletRequest request){
        return userService.update(id, user, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id, HttpServletRequest request){
        return userService.delete(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id, HttpServletRequest request){
        return userService.findById(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Object> findAll(HttpServletRequest request){
        Pageable pageable = PageRequest.of(0, OtherConfig.getDefaultPaginationSize(), Sort.by("id"));
        return userService.findAll(pageable, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{sort}/{sort_by}/{page}")
    public ResponseEntity<Object> findByParam(@PathVariable String sort,
                                              @PathVariable(value = "sort_by") String sortBy,
                                              @PathVariable Integer page,
                                              @RequestParam String column,
                                              @RequestParam String value,
                                              @RequestParam Integer size,
                                              HttpServletRequest request){
        Pageable pageable = null;
        sortBy = sortByColumn(sortBy);
        if(sort.equals("asc")){
            pageable = PageRequest.of(page, size, Sort.by(sortBy));
        }else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }
        return userService.findByParam(pageable, column, value, request);
    }

    private String sortByColumn(String sortBy){
        switch (sortBy){
            case "name": sortBy = "name"; break;
            case "email": sortBy = "email"; break;
            case "address": sortBy = "address"; break;
            case "postCode":  sortBy = "postCode"; break;
            case "role":  sortBy = "role"; break;
            default: sortBy = "id"; break;
        }
        return sortBy;
    }

}
