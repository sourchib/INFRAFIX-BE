package com.infrafix.citizen_reporting.core;

import com.infrafix.citizen_reporting.model.Report;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface IReportService<REQ> {
    ResponseEntity<Object> createReport(REQ req, HttpServletRequest request);

    ResponseEntity<Object> getAllReports(HttpServletRequest request);

    ResponseEntity<Object> getReportById(Long id, HttpServletRequest request);

    ResponseEntity<Object> deleteReport(Long id, HttpServletRequest request);

    ResponseEntity<Object> changeStatus(Long reportId, Long statusId, HttpServletRequest request);

    ResponseEntity<Object> findByParam(Pageable pageable, String column, String value, HttpServletRequest request);

    void generateReportPDF(HttpServletResponse response, Report report) throws IOException;
}
