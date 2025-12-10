package com.infrafix.citizen_reporting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Title", nullable = false, length = 50)
    private String title;

    @Column(name = "Description", nullable = false, length = 100)
    private String description;

    @Column(name = "Street", nullable = false, length = 100)
    private String street;

    @Column(name = "City", nullable = false, length = 50)
    private String city;

    @Column(name = "Province", nullable = false, length = 50)
    private String province;

    @Column(name = "PostCode", nullable = false, length = 10)
    private String postCode;

    @Column(name = "CreatedBy", nullable = false, updatable = false)
    private Long createdBy = 0L;

    @Column(name = "CreatedDate", nullable = false, updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
    }

    @Column(name = "ModifiedBy")
    private Long modifiedBy = 0L;

    @Column(name = "ModifiedDate")
    @UpdateTimestamp
    private LocalDateTime modifiedDate = LocalDateTime.now();

    // Many to One Relation With User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID")
    @JsonIgnore
    private User user;

    // Many to One Relation With Status
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StatusID")
    @JsonIgnore
    private Status status;

    // Getter Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
}
