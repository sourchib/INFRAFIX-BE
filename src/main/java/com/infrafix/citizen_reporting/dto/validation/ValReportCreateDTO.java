package com.infrafix.citizen_reporting.dto.validation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ValReportCreateDTO {

    @NotBlank(message = "Title is Required")
    @Size(max = 50, message = "Title Max Length is 50")
    private String title;

    @NotBlank(message = "Description is Required")
    @Size(max = 100, message = "Description Max Length is 100")
    private String description;

    @NotBlank(message = "Street is required")
    @Size(max = 100, message = "Street max length is 100")
    private String street;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City max length is 50")
    private String city;

    @NotBlank(message = "Province is required")
    @Size(max = 50, message = "Province max length is 50")
    private String province;

    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "^\\d{5}$", message = "Please Enter Valid Postal Code")
    private String postCode;


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
