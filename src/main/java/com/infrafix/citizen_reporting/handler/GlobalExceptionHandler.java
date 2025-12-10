package com.infrafix.citizen_reporting.handler;

import com.infrafix.citizen_reporting.util.GlobalResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex,
                        HttpServletRequest request) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getAllErrors().forEach((error) -> {
                        String fieldName = ((FieldError) error).getField();
                        String errorMessage = error.getDefaultMessage();
                        errors.put(fieldName, errorMessage);
                });

                return GlobalResponse.badRequest(errors, request);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handleGeneralException(Exception ex, HttpServletRequest request) {
                // Log the full exception for debugging
                ex.printStackTrace();
                return GlobalResponse.internalServerError(ex.getMessage(), request);
        }
}
