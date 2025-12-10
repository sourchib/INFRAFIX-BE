package com.infrafix.citizen_reporting.service;

import com.infrafix.citizen_reporting.core.ITechnicianService;
import com.infrafix.citizen_reporting.dto.response.TechnicianResponseDTO;
import com.infrafix.citizen_reporting.handler.GlobalExceptionHandler;
import com.infrafix.citizen_reporting.model.Report;
import com.infrafix.citizen_reporting.model.Status;
import com.infrafix.citizen_reporting.model.Technician;
import com.infrafix.citizen_reporting.model.User;
import com.infrafix.citizen_reporting.repo.ReportRepository;
import com.infrafix.citizen_reporting.repo.StatusRepository;
import com.infrafix.citizen_reporting.repo.TechnicianRepository;
import com.infrafix.citizen_reporting.repo.UserRepository;
import com.infrafix.citizen_reporting.security.JwtContextUtil;
import com.infrafix.citizen_reporting.util.GlobalResponse;
import com.infrafix.citizen_reporting.util.TransformPagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * platform code : IF
 * module code : TS
 */

@Service
@Transactional
public class TechnicianService implements ITechnicianService<Long, Technician, TechnicianResponseDTO> {

    private final TechnicianRepository technicianRepository;
    private final ReportRepository reportRepo;
    private final UserRepository userRepo;
    private final StatusRepository statusRepo;
    private final GlobalExceptionHandler globalExceptionHandler;
    private final TransformPagination tp;
    private final JwtContextUtil jwtContextUtil;

    public TechnicianService(
            TechnicianRepository technicianRepository,
            ReportRepository reportRepo,
            UserRepository userRepo,
            StatusRepository statusRepo,
            GlobalExceptionHandler globalExceptionHandler,
            TransformPagination tp,
            JwtContextUtil jwtContextUtil) {
        this.technicianRepository = technicianRepository;
        this.reportRepo = reportRepo;
        this.userRepo = userRepo;
        this.statusRepo = statusRepo;
        this.globalExceptionHandler = globalExceptionHandler;
        this.tp = tp;
        this.jwtContextUtil = jwtContextUtil;
    }

    // ASSIGN TECHNICIAN

    @Override
    public ResponseEntity<Object> assignTechnician(Long reportId, Long technicianId, HttpServletRequest request) {
        try {
            Long userId = jwtContextUtil.getCurrentUserId(request); // Get the admin ID

            Report report = reportRepo.findById(reportId)
                    .orElseThrow(() -> new RuntimeException("Report not found"));

            User technician = userRepo.findById(technicianId)
                    .orElseThrow(() -> new RuntimeException("Technician not found"));

            if (!technician.getRole().getRole().equalsIgnoreCase("technician")) {
                return GlobalResponse.notTechnician("IFTSE010", request);
            }

            // Validate status 2 (IN_PROGRESS)
            Status inProgress = statusRepo.findById(2L)
                    .orElseThrow(() -> new RuntimeException("Status IN_PROGRESS not found"));

            // Close previous technician assignment
            Technician activeAssignment = technicianRepository.findByReportIdAndUnassignedAtIsNull(reportId)
                    .orElse(null);

            if (activeAssignment != null) {
                activeAssignment.setUnassignedAt(LocalDateTime.now());
                technicianRepository.save(activeAssignment);
            }

            // Update status and modifiedBy
            report.setStatus(inProgress);
            report.setModifiedBy(userId);
            reportRepo.save(report);

            // Create new technician assignment
            Technician newAssignment = new Technician();
            newAssignment.setReport(report);
            newAssignment.setTechnician(technician);

            Technician saved = technicianRepository.save(newAssignment);

            return GlobalResponse.dataFound(toDTO(saved), request);

        } catch (Exception e) {
            return GlobalResponse.assignFailed("IFTSE020", request);
        }
    }

    // UNASSIGN TECHNICIAN

    @Override
    public ResponseEntity<Object> unassignTechnician(Long reportId, HttpServletRequest request) {
        try {
            Long userId = jwtContextUtil.getCurrentUserId(request); // Who is unassigning

            // Find active assignment
            Technician assignment = technicianRepository
                    .findByReportIdAndUnassignedAtIsNull(reportId)
                    .orElseThrow(() -> new RuntimeException("No active assignment found"));

            assignment.setUnassignedAt(LocalDateTime.now());
            technicianRepository.save(assignment);

            // Update modifiedBy on the report
            Report report = reportRepo.findById(reportId)
                    .orElseThrow(() -> new RuntimeException("Report not found"));
            report.setModifiedBy(userId);
            reportRepo.save(report);

            return ResponseEntity.ok(GlobalResponse.assignSuccess(request));

        } catch (Exception ex) {
            return globalExceptionHandler.handleGeneralException(ex, request);
        }
    }

    // GET ASSIGNMENT BY ID

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        try {
            Technician assignment = technicianRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Technician assignment not found"));

            return GlobalResponse.dataFound(toDTO(assignment), request);

        } catch (Exception e) {
            return globalExceptionHandler.handleGeneralException(e, request);
        }
    }

    // FIND ALL

    @Override
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        try {
            Page<Technician> page = technicianRepository.findAll(pageable);

            if (page.isEmpty()) {
                return GlobalResponse.dataNotFound("IFTSE041", request);
            }

            List<TechnicianResponseDTO> listDTO = page.getContent()
                    .stream()
                    .map(this::toDTO)
                    .toList();

            Map<String, Object> data = tp.transformPagination(listDTO, page, "id", "");

            return GlobalResponse.dataFound(data, request);

        } catch (Exception e) {
            return GlobalResponse.internalServerError("IFTSE042", request);
        }
    }

    // DTO

    public TechnicianResponseDTO toDTO(Technician assignment) {

        TechnicianResponseDTO dto = new TechnicianResponseDTO();

        dto.setId(assignment.getId());
        dto.setReportId(assignment.getReport().getId());
        dto.setAssignedAt(assignment.getAssignedAt());
        dto.setUnassignedAt(assignment.getUnassignedAt());

        TechnicianResponseDTO.TechnicianSummary tech = new TechnicianResponseDTO.TechnicianSummary(
                assignment.getTechnician().getId(),
                assignment.getTechnician().getName(),
                assignment.getTechnician().getEmail());

        dto.setTechnician(tech);

        return dto;
    }
}
