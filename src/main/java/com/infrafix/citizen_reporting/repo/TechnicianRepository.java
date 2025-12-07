package com.infrafix.citizen_reporting.repo;

import com.infrafix.citizen_reporting.model.Technician;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechnicianRepository extends JpaRepository<Technician, Long> {
    Optional<Technician> findByReportIdAndUnassignedAtIsNull(Long reportId); //Find technician that is active
}
