package com.infrafix.citizen_reporting.handler;

import com.infrafix.citizen_reporting.util.LoggingFile;
import com.infrafix.citizen_reporting.util.RequestCapture;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String CLASS_NAME = "GlobalExceptionHandler";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );

        return new ResponseHandler().handleResponse(
                "Validation Error",
                HttpStatus.BAD_REQUEST,
                errors,
                "X04000",   // you can use any code here
                request
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolations(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(cv ->
                errors.put(cv.getPropertyPath().toString(), cv.getMessage())
        );

        return new ResponseHandler().handleResponse(
                "Validation Error",
                HttpStatus.BAD_REQUEST,
                errors,
                "X04001",
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(
            Exception ex,
            HttpServletRequest request
    ) {

        LoggingFile.logException(
                CLASS_NAME,
                "handleAllExceptions : " + RequestCapture.allRequest(request),
                ex
        );

        return new ResponseHandler().handleResponse(
                "Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR,
                null,
                "X05999",
                request
        );
    }
}
