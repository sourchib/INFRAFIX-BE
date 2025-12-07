package com.infrafix.citizen_reporting.controller;

import com.infrafix.citizen_reporting.dto.request.TechnicianRequestDTO;
import com.infrafix.citizen_reporting.service.TechnicianService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tech")
public class TechnicianController {

    private final TechnicianService technicianService;

    public TechnicianController(TechnicianService technicianService) {
        this.technicianService = technicianService;
    }


    // ASSIGN TECHNICIAN

    @PostMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> assign(
            @RequestBody TechnicianRequestDTO technicianDTO,
            HttpServletRequest request
    ) {
        return technicianService.assignTechnician(
                technicianDTO.getReportId(),
                technicianDTO.getTechnicianId(),
                request
        );
    }


    // UNASSIGN TECHNICIAN

    @PostMapping("/unassign/{reportId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> unassign(
            @PathVariable Long reportId,
            HttpServletRequest request
    ) {
        return technicianService.unassignTechnician(reportId, request);
    }


    // FIND ALL ASSIGNMENTS
    @GetMapping("/assignment/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        return technicianService.findAll(pageable, request);
    }


    // FIND ASSIGNMENT BY ID
    @GetMapping("/assignment/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> findById(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        return technicianService.findById(id, request);
    }
}
