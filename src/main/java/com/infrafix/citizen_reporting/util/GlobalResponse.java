package com.infrafix.citizen_reporting.util;

import com.infrafix.citizen_reporting.handler.ResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GlobalResponse {
    public static <T> ResponseEntity<T> created(T data, HttpServletRequest request) {
        return (ResponseEntity<T>) new ResponseHandler().handleResponse("DATA SUCCESSFULLY CREATED", HttpStatus.CREATED,
                data, null, request);
    }

    public static ResponseEntity<Object> dataCreationFailed(String errorCode, HttpServletRequest request) {
        return new ResponseHandler().handleResponse("CREATION FAILED", HttpStatus.BAD_REQUEST, null, errorCode,
                request);
    }

    public static ResponseEntity<Object> dataUpdate(HttpServletRequest request) {
        return new ResponseHandler().handleResponse("DATA SUCCESSFULLY UPDATED", HttpStatus.OK, null, null, request);
    }

    public static ResponseEntity<Object> dataUpdateFailed(String errorCode, HttpServletRequest request) {
        return new ResponseHandler().handleResponse("UPDATE FAILED", HttpStatus.INTERNAL_SERVER_ERROR, null, errorCode,
                request);
    }

    public static ResponseEntity<Object> dataDeletion(HttpServletRequest request) {
        return new ResponseHandler().handleResponse("DATA SUCCESSFULLY DELETED", HttpStatus.OK, null, null, request);
    }

    public static ResponseEntity<Object> dataDeletionFailed(String errorCode, HttpServletRequest request) {
        return new ResponseHandler().handleResponse("DELETION FAILED", HttpStatus.INTERNAL_SERVER_ERROR, null,
                errorCode, request);
    }

    public static <T> ResponseEntity<T> internalServerError(String errorCode, HttpServletRequest request) {
        return (ResponseEntity<T>) new ResponseHandler().handleResponse("INTERNAL SERVER ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR, null, errorCode, request);
    }

    public static ResponseEntity<Object> dataFound(Object data, HttpServletRequest request) {
        return new ResponseHandler().handleResponse("DATA FOUND", HttpStatus.OK, data, null, request);
    }

    public static ResponseEntity<Object> dataNotFound(String errorCode, HttpServletRequest request) {
        return new ResponseHandler().handleResponse("DATA NOT FOUND", HttpStatus.NOT_FOUND, null, errorCode, request);
    }

    public static ResponseEntity<Object> notAdmin(String errorCode, HttpServletRequest request) {
        return new ResponseHandler().handleResponse("NOT AN ADMIN", HttpStatus.UNAUTHORIZED, null, errorCode, request);
    }

    public static ResponseEntity<Object> incorrectPassword(String errorCode, HttpServletRequest request) {
        return new ResponseHandler().handleResponse("INCORRECT PASSWORD", HttpStatus.UNAUTHORIZED, null, errorCode,
                request);
    }

    public static ResponseEntity<Object> notTechnician(String errorCode, HttpServletRequest request) {
        return new ResponseHandler().handleResponse("NOT TECHNICIAN", HttpStatus.UNAUTHORIZED, null, errorCode,
                request);
    }

    public static ResponseEntity<Object> assignSuccess(HttpServletRequest request) {
        return new ResponseHandler().handleResponse("ASSIGN SUCCESSFULLY", HttpStatus.OK, null, null, request);
    }

    public static ResponseEntity<Object> assignFailed(String errorCode, HttpServletRequest request) {
        return new ResponseHandler().handleResponse("ASSIGN FAILED", HttpStatus.INTERNAL_SERVER_ERROR, null, errorCode,
                request);
    }

    public static ResponseEntity<Object> unauthorized(String errorCode, HttpServletRequest request) {
        return new ResponseHandler().handleResponse("UNAUTHORIZED", HttpStatus.UNAUTHORIZED, null, errorCode, request);
    }

    public static <T> ResponseEntity<T> badRequest(String errorCode, HttpServletRequest request) {
        return (ResponseEntity<T>) new ResponseHandler().handleResponse("BAD REQUEST", HttpStatus.BAD_REQUEST, null,
                errorCode, request);
    }

    public static ResponseEntity<Object> badRequest(Object data, HttpServletRequest request) {
        return new ResponseHandler().handleResponse("BAD REQUEST", HttpStatus.BAD_REQUEST, data, "VALIDATION_JSON",
                request);
    }
}
