package com.infrafix.citizen_reporting.controller;

import com.infrafix.citizen_reporting.dto.validation.ValReportCreateDTO;
import com.infrafix.citizen_reporting.model.Report;
import com.infrafix.citizen_reporting.repo.ReportRepository;
import com.infrafix.citizen_reporting.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private final ReportRepository reportRepo;

    public ReportController(ReportService reportService, ReportRepository reportRepo) {
        this.reportService = reportService;
        this.reportRepo = reportRepo;
    }

    // CREATE REPORT

    @PostMapping("/create")
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<Object> create(
            @Valid @RequestBody ValReportCreateDTO dto,
            HttpServletRequest request) {
        return reportService.createReport(dto, request);
    }

    // FIND ALL

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(HttpServletRequest request) {
        return reportService.getAllReports(request);
    }

    // FIND BY ID

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN', 'CITIZEN')")
    public ResponseEntity<Object> findById(
            @PathVariable Long id,
            HttpServletRequest request) {
        return reportService.getReportById(id, request);
    }

    // GENERAL STATUS UPDATE

    // DELETE REPORT
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Object> delete(
            @PathVariable Long id,
            HttpServletRequest request) {
        return reportService.deleteReport(id, request);
    }

    // GENERAL STATUS UPDATE

    @PutMapping("/{id}/status/{statusId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateStatus(
            @PathVariable Long id,
            @PathVariable Long statusId,
            HttpServletRequest request) {
        return reportService.changeStatus(id, statusId, request);
    }

    // COMPLETE REPORT

    @PutMapping("/complete/{reportId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    public ResponseEntity<Object> completeReport(
            @PathVariable Long reportId,
            HttpServletRequest request) {
        return reportService.changeStatus(reportId, 3L, request);
    }

    // CANCEL REPORT

    @PutMapping("/cancel/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> cancelReport(
            @PathVariable Long id,
            HttpServletRequest request) {
        return reportService.changeStatus(id, 4L, request);
    }

    // FIND BY PARAM

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{sort}/{sort_by}/{page}")
    public ResponseEntity<Object> findByParam(@PathVariable String sort,
            @PathVariable("sort_by") String sortBy,
            @PathVariable Integer page,
            @RequestParam String column,
            @RequestParam String value,
            @RequestParam Integer size,
            HttpServletRequest request) {
        Pageable pageable;
        sortBy = sortByColumn(sortBy);
        if (sort.equalsIgnoreCase("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }
        return reportService.findByParam(pageable, column, value, request);
    }

    // DOWNLOAD PDF

    @GetMapping("/download/{reportId}")
    public void downloadReportPdf(@PathVariable Long reportId, HttpServletResponse response) throws IOException {
        Report report = reportRepo.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        reportService.generateReportPDF(response, report);
    }

    private String sortByColumn(String sortBy) {
        return switch (sortBy.toLowerCase()) {
            case "title" -> "title";
            case "created" -> "createdDate";
            case "updated" -> "modifiedDate";
            default -> "id";
        };
    }
}