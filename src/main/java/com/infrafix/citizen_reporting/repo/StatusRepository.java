package com.infrafix.citizen_reporting.repo;

import com.infrafix.citizen_reporting.model.Status;
import org.springframework.data.repository.CrudRepository;

public interface StatusRepository extends CrudRepository<Status, Long> {
}
