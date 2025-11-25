package com.infrafix.citizen_reporting.util;

import com.infrafix.citizen_reporting.handler.ResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GlobalResponse {
    public static ResponseEntity<Object> dataCreation (HttpServletRequest request){
        return new ResponseHandler().handleResponse("DATA SUCCESSFULLY CREATED", HttpStatus.CREATED, null, null, request);
    }

    public static ResponseEntity<Object> dataCreationFailed (String errorCode, HttpServletRequest request){
        return new ResponseHandler().handleResponse("CREATION FAILED", HttpStatus.BAD_REQUEST, null, errorCode, request);
    }

    public static ResponseEntity<Object> dataUpdate (HttpServletRequest request){
        return new ResponseHandler().handleResponse("DATA SUCCESSFULLY UPDATED", HttpStatus.OK, null, null, request);
    }

    public static ResponseEntity<Object> dataUpdateFailed (String errorCode, HttpServletRequest request){
        return new ResponseHandler().handleResponse("UPDATE FAILED", HttpStatus.INTERNAL_SERVER_ERROR, null, errorCode, request);
    }

    public static ResponseEntity<Object> dataDeletion (HttpServletRequest request){
        return new ResponseHandler().handleResponse("DATA SUCCESSFULLY DELETED", HttpStatus.OK, null, null, request);
    }

    public static ResponseEntity<Object> dataDeletionFailed (String errorCode, HttpServletRequest request){
        return new ResponseHandler().handleResponse("DELETION FAILED", HttpStatus.INTERNAL_SERVER_ERROR, null, errorCode, request);
    }

    public static ResponseEntity<Object> errorOccurred (Object data, HttpServletRequest request){
        return new ResponseHandler().handleResponse("ERROR OCCURRED", HttpStatus.INTERNAL_SERVER_ERROR, null, data, request);
    }

    public static ResponseEntity<Object> dataFound (Object data, HttpServletRequest request){
        return new ResponseHandler().handleResponse("DATA FOUND", HttpStatus.OK, data, null, request);
    }

    public static ResponseEntity<Object> dataNotFound (String errorCode, HttpServletRequest request){
        return new ResponseHandler().handleResponse("DATA NOT FOUND", HttpStatus.NOT_FOUND, null, errorCode, request);
    }


}
