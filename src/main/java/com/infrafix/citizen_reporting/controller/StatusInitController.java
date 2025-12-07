package com.infrafix.citizen_reporting.controller;

import com.infrafix.citizen_reporting.model.Status;
import com.infrafix.citizen_reporting.repo.StatusRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/status")
public class StatusInitController {

    private final StatusRepository statusRepository;

    public StatusInitController(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @PostMapping("/init")
    public String initializeStatuses() {

        // Prevent duplicate initialization
        if (statusRepository.count() > 0) {
            return "Statuses already initialized.";
        }

        List<Status> statuses = Arrays.asList(
                createStatus("Pending"),
                createStatus("In Progress"),
                createStatus("Completed"),
                createStatus("Rejected")
        );

        statusRepository.saveAll(statuses);

        return "Default statuses created successfully.";
    }

    private Status createStatus(String name) {
        Status s = new Status();
        s.setStatus(name);
        return s;
    }
}
