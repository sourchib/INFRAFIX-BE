package com.infrafix.citizen_reporting.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("error-code", "X01001");
        errorResponse.put("error", "Otentikasi Bermasalah");
        errorResponse.put("status", 401);
        errorResponse.put("timestamp", OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        response.getWriter().write(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(errorResponse));
    }
}
