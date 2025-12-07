package com.infrafix.citizen_reporting.dto.validation;

import jakarta.validation.constraints.*;

public class ValUserCreateDTO {
    @NotBlank(message = "Name is Required")
    @Pattern(regexp = "^[a-zA-Z\\s]{5,50}$", message = "Name Only Can Only Contain Letters and Spaces With Length 5 - 50. Ex : Infrastructure Fix")
    private String name;

    @NotBlank(message = "Email is Required")
    @Size(max = 30, message = "Email must not exceed 30 characters")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Please Use a Valid Email")
    private String email;

    @NotBlank(message = "Password is Required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$", message = "Password Should Contain 8 - 20 Characters, 1 of Uppercase, Lowercase, Number, and Special Character (@$!%*?&)")
    private String password;

    @NotBlank(message = "Phone Number is Required")
    @Size(max = 13, message = "Phone Number must not exceed 13 characters")
    @Pattern(regexp = "^(\\+62|62|0)8\\d{7,13}$", message = "Use Valid Phone Number. Ex : +6281234567890 Or 6281234567890 Or 081234567890")
    private String phoneNumber;

    @NotBlank(message = "Address is Required")
    @Pattern(regexp = "^[A-Za-z0-9.,\\-\\/() ]{5,100}$", message = "Please Enter a Valid Address")
    private String address;

    @NotBlank(message = "Postal Code is Required")
    @Pattern(regexp = "^\\d{5}$", message = "Please Enter Valid Postal Code")
    private String postCode;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
