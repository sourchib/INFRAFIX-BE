package com.infrafix.citizen_reporting.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public ResponseEntity<Object> handleResponse(
            String message,
            HttpStatus status,
            Object data,
            Object errorCode,
            HttpServletRequest request
    ){
        Map<String, Object> m = new HashMap<>();
        m.put("message", message);
        m.put("status", status.value());
        m.put("data", data == null ? "" : data);
        m.put("timestamp", Instant.now().toString());
        m.put("success", !status.isError());
        if(errorCode != null){
            m.put("errorcode", errorCode);
            m.put("path", request.getRequestURI());
        }
        return new ResponseEntity<>(m, status);
    }
}
