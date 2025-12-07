package com.infrafix.citizen_reporting.service;

import com.infrafix.citizen_reporting.core.IReportService;
import com.infrafix.citizen_reporting.dto.response.ReportResponseDTO;
import com.infrafix.citizen_reporting.dto.validation.ValReportCreateDTO;
import com.infrafix.citizen_reporting.model.Report;
import com.infrafix.citizen_reporting.model.Status;
import com.infrafix.citizen_reporting.model.User;
import com.infrafix.citizen_reporting.repo.ReportRepository;
import com.infrafix.citizen_reporting.repo.StatusRepository;
import com.infrafix.citizen_reporting.repo.UserRepository;
import com.infrafix.citizen_reporting.security.JwtContextUtil;
import com.infrafix.citizen_reporting.util.GlobalResponse;
import com.infrafix.citizen_reporting.util.LoggingFile;
import com.infrafix.citizen_reporting.util.RequestCapture;
import com.infrafix.citizen_reporting.util.TransformPagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.itextpdf.html2pdf.HtmlConverter;
import jakarta.servlet.http.HttpServletResponse;
import com.itextpdf.html2pdf.ConverterProperties;

import java.io.IOException;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * platform code : IF
 * module code : RS
 */

@Service
@Transactional
public class ReportService implements IReportService<ValReportCreateDTO> {

    private final ReportRepository reportRepo;
    private final StatusRepository statusRepo;
    private final UserRepository userRepo;
    private final JwtContextUtil jwtContextUtil;
    private final TechnicianService technicianService;
    private final TransformPagination tp;

    private static final String className = "ReportService";


    public ReportService(
            ReportRepository reportRepo,
            StatusRepository statusRepo,
            UserRepository userRepo,
            JwtContextUtil jwtContextUtil,
            TechnicianService technicianService,
            TransformPagination tp
    ) {
        this.reportRepo = reportRepo;
        this.statusRepo = statusRepo;
        this.userRepo = userRepo;
        this.jwtContextUtil = jwtContextUtil;
        this.technicianService = technicianService;
        this.tp = tp;
    }


    // CREATE REPORT

    @Override
    public ResponseEntity<Object> createReport(ValReportCreateDTO dto, HttpServletRequest request) {
        try {
            Long userId = jwtContextUtil.getCurrentUserId(request);

            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Status status = statusRepo.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Default status not found"));

            Report report = new Report();
            report.setTitle(dto.getTitle());
            report.setDescription(dto.getDescription());
            report.setStreet(dto.getStreet());
            report.setCity(dto.getCity());
            report.setProvince(dto.getProvince());
            report.setPostCode(dto.getPostCode());
            report.setUser(user);
            report.setStatus(status);
            report.setCreatedBy(userId);

            Report savedReport = reportRepo.save(report);
            ReportResponseDTO reportResponseDTO = toDTO(savedReport);

            return GlobalResponse.created(reportResponseDTO, request);

        } catch (Exception e) {
            LoggingFile.logException(className, "createReport(ValReportCreateDTO dto, HttpServletRequest request)", e);
            return GlobalResponse.dataCreationFailed("IFRSE01", request);
        }
    }


    // GET ALL REPORTS

    @Override
    public ResponseEntity<Object> getAllReports(HttpServletRequest request) {
        try {
            var reports = reportRepo.findAll();

            if (reports.isEmpty()) {
                return GlobalResponse.dataNotFound("IFRSV01", request);
            }

            List<ReportResponseDTO> dtoList = reports.stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());

            return GlobalResponse.dataFound(dtoList, request);

        } catch (Exception e) {
            LoggingFile.logException(className, "getAllReports(HttpServletRequest request)", e);
            return GlobalResponse.internalServerError("IFRSE02", request);
        }
    }


    // GET REPORT BY ID

    @Override
    public ResponseEntity<Object> getReportById(Long id, HttpServletRequest request) {
        try {
            Report report = reportRepo.findById(id).orElse(null);

            if (report == null) {
                return GlobalResponse.dataNotFound("IFRSV02", request);
            }

            ReportResponseDTO dto = toDTO(report);

            return GlobalResponse.dataFound(dto, request);

        } catch (Exception e) {
            LoggingFile.logException(className, "getReportById(Long id, HttpServletRequest request)", e);
            return GlobalResponse.internalServerError("IFRSE03", request);
        }
    }


    // GENERAL CHANGE STATUS

    @Override
    public ResponseEntity<Object> changeStatus(Long reportId, Long statusId, HttpServletRequest request) {
        try {
            Long userId = jwtContextUtil.getCurrentUserId(request);

            Report report = reportRepo.findById(reportId).orElse(null);
            if (report == null) {
                return GlobalResponse.dataNotFound("IFRSV03", request);
            }

            Status newStatus = statusRepo.findById(statusId).orElse(null);
            if (newStatus == null) {
                return GlobalResponse.badRequest("IFRSV04", request);
            }

            // Unassign technician if report is completed or cancelled
            if (statusId == 3L || statusId == 4L) {
                technicianService.unassignTechnician(reportId, request);
            }

            report.setStatus(newStatus);
            report.setModifiedBy(userId);
            reportRepo.save(report);

            return GlobalResponse.dataUpdate(request);

        } catch (Exception e) {
            LoggingFile.logException(className, "changeStatus(Long reportId, Long statusId, HttpServletRequest request)", e);
            return GlobalResponse.dataUpdateFailed("IFRSE04", request);
        }
    }


    // FIND BY PARAM

    @Override
    public ResponseEntity<Object> findByParam(
            Pageable pageable,
            String column,
            String value,
            HttpServletRequest request
    ) {
        try {
            Page<Report> page;

            switch (column) {
                case "title" ->
                        page = reportRepo.findByTitleContainsIgnoreCase(pageable, value);

                case "status" -> {
                    Long statusId;
                    try {
                        statusId = Long.parseLong(value); // convert request param to Long
                    } catch (NumberFormatException e) {
                        return GlobalResponse.badRequest("IFRSV052", request); // invalid number
                    }
                    page = reportRepo.findByStatusId(pageable, statusId);
                }

                default ->
                        page = reportRepo.findAllByOrderByCreatedDate(pageable);
            }

            if (page.isEmpty()) {
                return GlobalResponse.dataNotFound("IFRSV051", request);
            }

            List<ReportResponseDTO> listDTO = page.getContent()
                    .stream()
                    .map(this::toDTO)
                    .toList();

            Map<String, Object> data =
                    tp.transformPagination(listDTO, page, column, value);

            return GlobalResponse.dataFound(data, request);

        } catch (Exception e) {
            LoggingFile.logException(
                    className,
                    "findByParam(Pageable pageable, String column, String value, HttpServletRequest request) " +
                            RequestCapture.allRequest(request),
                    e
            );
            return GlobalResponse.internalServerError("IFRSE051", request);
        }
    }


    // REPORT PDF

    @Override
    public void generateReportPDF(HttpServletResponse response, Report report) throws IOException {
        // Prepare HTML with CSS
        String html = buildHtml(report);

        // Configure HTML to PDF converter
        ConverterProperties converterProperties = new ConverterProperties();

        // Write PDF to response output stream
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=report_" + report.getId() + ".pdf");

        HtmlConverter.convertToPdf(html, response.getOutputStream(), converterProperties);
    }


    // HTML FOR PDF

    private String buildHtml(Report report) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

        return String.format("""
        <html>
        <head>
            <style>
                body { font-family: Arial, sans-serif; margin: 40px; color: #333; }
                h1 { text-align: center; color: #1a73e8; margin-bottom: 5px; }
                .subtitle { text-align: center; font-size: 14px; color: #555; margin-bottom: 30px; }
                table { width: 100%%; border-collapse: collapse; margin-top: 20px; }
                table, th, td { border: 1px solid #ccc; }
                th, td { padding: 12px; text-align: left; }
                th { background-color: #f2f2f2; }
                .footer { margin-top: 50px; text-align: center; font-size: 12px; color: #777; }
            </style>
        </head>
        <body>
            <h1>Citizen Report</h1>
            <div class="subtitle">Report ID: %d</div>

            <table>
                <tr><th>Title</th><td>%s</td></tr>
                <tr><th>Description</th><td>%s</td></tr>
                <tr><th>Street</th><td>%s</td></tr>
                <tr><th>City</th><td>%s</td></tr>
                <tr><th>Province</th><td>%s</td></tr>
                <tr><th>Post Code</th><td>%s</td></tr>
                <tr><th>Status</th><td>%s</td></tr>
                <tr><th>Created By</th><td>%s</td></tr>
                <tr><th>Created Date</th><td>%s</td></tr>
            </table>

            <div class="footer">
                Generated by InfraFix System &copy; 2025
            </div>
        </body>
        </html>
        """,
                report.getId(),
                report.getTitle(),
                report.getDescription(),
                report.getStreet(),
                report.getCity(),
                report.getProvince(),
                report.getPostCode(),
                report.getStatus().getStatus(),
                report.getUser().getName(),
                report.getCreatedDate().format(formatter)
        );
    }



    // DTO MAPPING HELPER

    public ReportResponseDTO toDTO(Report report) {

        ReportResponseDTO dto = new ReportResponseDTO();
        dto.setId(report.getId());
        dto.setTitle(report.getTitle());
        dto.setDescription(report.getDescription());
        dto.setStreet(report.getStreet());
        dto.setCity(report.getCity());
        dto.setProvince(report.getProvince());
        dto.setPostCode(report.getPostCode());

        dto.setStatus(report.getStatus().getStatus());
        dto.setCreatedDate(report.getCreatedDate());

        ReportResponseDTO.UserSummary userSummary =
                new ReportResponseDTO.UserSummary(
                        report.getUser().getId(),
                        report.getUser().getName()
                );

        dto.setCreatedBy(userSummary);

        return dto;
    }
}
