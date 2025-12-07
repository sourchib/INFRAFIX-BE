package com.infrafix.citizen_reporting.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class JwtContextUtil {

    private final JwtUtility jwtUtility;

    public JwtContextUtil(JwtUtility jwtUtility) {
        this.jwtUtility = jwtUtility;
    }

    public Long getCurrentUserId(HttpServletRequest request) {
        String token = jwtUtility.resolveToken(request);

        if (token == null) {
            throw new RuntimeException("Missing JWT token");
        }

        Claims claims = jwtUtility.getAllClaimsFromToken(token);

        return Long.valueOf(claims.get("userId").toString());
    }
}
