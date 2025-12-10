package com.infrafix.citizen_reporting.controller;

import com.infrafix.citizen_reporting.dto.request.TechnicianRequestDTO;
import com.infrafix.citizen_reporting.service.ReportService;
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
    private final ReportService reportService;

    public TechnicianController(TechnicianService technicianService, ReportService reportService) {
        this.technicianService = technicianService;
        this.reportService = reportService;
    }

    // FILTER REPORTS (For Technicians)
    @PreAuthorize("hasAnyRole('TECHNICIAN', 'ADMIN')")
    @GetMapping("/reports/{sort}/{sort_by}/{page}")
    public ResponseEntity<Object> filterReports(
            @PathVariable String sort,
            @PathVariable("sort_by") String sortBy,
            @PathVariable Integer page,
            @RequestParam String column,
            @RequestParam String value,
            @RequestParam Integer size,
            HttpServletRequest request) {
        org.springframework.data.domain.Pageable pageable;
        sortBy = sortByColumn(sortBy);
        if (sort.equalsIgnoreCase("asc")) {
            pageable = org.springframework.data.domain.PageRequest.of(page, size,
                    org.springframework.data.domain.Sort.by(sortBy));
        } else {
            pageable = org.springframework.data.domain.PageRequest.of(page, size,
                    org.springframework.data.domain.Sort.by(sortBy).descending());
        }
        return reportService.findByParam(pageable, column, value, request);
    }

    private String sortByColumn(String sortBy) {
        return switch (sortBy.toLowerCase()) {
            case "title" -> "title";
            case "created" -> "createdDate";
            case "updated" -> "modifiedDate";
            default -> "id";
        };
    }

    // ASSIGN TECHNICIAN

    @PostMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> assign(
            @RequestBody TechnicianRequestDTO technicianDTO,
            HttpServletRequest request) {
        return technicianService.assignTechnician(
                technicianDTO.getReportId(),
                technicianDTO.getTechnicianId(),
                request);
    }

    // UNASSIGN TECHNICIAN

    @PostMapping("/unassign/{reportId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> unassign(
            @PathVariable Long reportId,
            HttpServletRequest request) {
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
            HttpServletRequest request) {
        return technicianService.findById(id, request);
    }
}
