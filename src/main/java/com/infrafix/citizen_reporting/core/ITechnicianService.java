package com.infrafix.citizen_reporting.core;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ITechnicianService<REQ, ENTITY, DTO> {
    ResponseEntity<Object> assignTechnician(Long reportId, REQ requestDTO, HttpServletRequest request);
    ResponseEntity<Object> unassignTechnician(Long reportId, HttpServletRequest request);
    ResponseEntity<Object> findById(Long id, HttpServletRequest request);
    ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request);
}
