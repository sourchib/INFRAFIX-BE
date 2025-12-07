package com.infrafix.citizen_reporting.dto.response;

import java.time.LocalDateTime;

public class TechnicianResponseDTO {
    private Long id;
    private Long reportId;
    private TechnicianSummary technician;
    private LocalDateTime assignedAt;
    private LocalDateTime unassignedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public TechnicianSummary getTechnician() {
        return technician;
    }

    public void setTechnician(TechnicianSummary technician) {
        this.technician = technician;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public LocalDateTime getUnassignedAt() {
        return unassignedAt;
    }

    public void setUnassignedAt(LocalDateTime unassignedAt) {
        this.unassignedAt = unassignedAt;
    }

    public static class TechnicianSummary{
        private Long id;
        private String name;
        private String email;

        public TechnicianSummary(){}

        public TechnicianSummary(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
        public Long getId(){
            return id;
        }
        public void setId(Long id){
            this.id = id;
        }
        public String getName(){
            return name;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getEmail(){
            return email;
        }
        public void setEmail(String email){
            this.email = email;
        }
    }
}
