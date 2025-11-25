package com.infrafix.citizen_reporting.repo;


import com.infrafix.citizen_reporting.model.Report;
import org.springframework.data.repository.CrudRepository;

public interface ReportRepository extends CrudRepository<Report, Long> {
}
