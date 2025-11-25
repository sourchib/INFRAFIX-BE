package com.infrafix.citizen_reporting.core;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IImageUpload {
    ResponseEntity<Object> uploadImage (MultipartFile file, HttpServletRequest request);
}
