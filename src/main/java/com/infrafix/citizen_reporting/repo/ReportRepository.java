package com.infrafix.citizen_reporting.repo;


import com.infrafix.citizen_reporting.model.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Page<Report> findAllByOrderByCreatedDate(Pageable pageable);
    Page<Report> findByTitleContainsIgnoreCase(Pageable pageable, String title);
    Page<Report> findByStatusId(Pageable pageable, Long statusId);
}
