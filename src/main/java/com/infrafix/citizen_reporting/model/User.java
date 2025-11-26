package com.infrafix.citizen_reporting.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name="Users",
uniqueConstraints = @UniqueConstraint(name = "idx-combination", columnNames = {"Name","Address"}),
indexes = @Index(name = "idx_email", columnList = "Email"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "Name", length = 50, nullable = false)
    private String name;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "Email", length = 30, nullable = false, unique = true)
    private String email;

    @Column(name = "PhoneNumber", length = 13, nullable = false)
    private String phoneNumber;

    @Column(name = "Address", nullable = false)
    private String address;

    @Column(name = "PostCode", length = 5, nullable = false, columnDefinition = "CHAR(5)")
    private String postCode;

    @Column(name = "CreatedBy", nullable = false, updatable = false)
    private Long createdBy = 1L;

    @Column(name = "CreatedDate", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "ModifiedBy")
    private Long modifiedBy;

    @Column(name = "ModifiedDate")
    @UpdateTimestamp
    private LocalDateTime modifiedDate;

    // Relation Many to One (Role)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RoleID")
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    // End of Relation Block

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
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
}
