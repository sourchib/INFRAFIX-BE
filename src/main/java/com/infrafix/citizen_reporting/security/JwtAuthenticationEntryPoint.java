package com.infrafix.citizen_reporting.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component("customAuthenticationEntryPoint")
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("data", null);
        errorResponse.put("success", true);
        errorResponse.put("message", "Welcome to INFRAFIX API v1.0");
        errorResponse.put("status", 200);

        response.getWriter().write(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(errorResponse));
    }
}
