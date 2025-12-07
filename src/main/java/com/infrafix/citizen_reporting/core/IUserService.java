package com.infrafix.citizen_reporting.core;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IUserService<REQ, ENTITY> {
    ResponseEntity<Object> createCitizen(REQ req, HttpServletRequest request);
    ResponseEntity<Object> update(Long id, ENTITY entity, HttpServletRequest request);
    ResponseEntity<Object> delete(Long id, HttpServletRequest request);
    ResponseEntity<Object> findById(Long id, HttpServletRequest request);
    ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request);
    ResponseEntity<Object> findByParam(Pageable pageable, String column, String value, HttpServletRequest request);
    ResponseEntity<Object> createTechnician(REQ req, HttpServletRequest request);
    ResponseEntity<Object> createAdmin(REQ req, HttpServletRequest request);
}