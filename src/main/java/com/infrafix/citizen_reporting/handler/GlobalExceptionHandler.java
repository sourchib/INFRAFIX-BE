package com.infrafix.citizen_reporting.handler;

import com.infrafix.citizen_reporting.util.LoggingFile;
import com.infrafix.citizen_reporting.util.RequestCapture;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String CLASS_NAME = "GlobalExceptionHandler";

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
                "Terjadi Kesalahan Di Server",
                HttpStatus.INTERNAL_SERVER_ERROR,
                null,
                "X05999",
                request
        );
    }
}
