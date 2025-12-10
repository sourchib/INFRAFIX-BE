package com.infrafix.citizen_reporting.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtility {

    private SecretKey key;

    @PostConstruct
    public void init() {
        // Generate a strong HS512 key (512+ bits)
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Map<String, Object> mappingBodyToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", claims.get("userId"));
        map.put("email", claims.getSubject());
        map.put("role", claims.get("role"));
        map.put("purpose", claims.get("purpose"));
        return map;
    }

    public String doGenerateToken(Map<String, Object> claims, String subject) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1800000)) // 30 min
                .signWith(key)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }


    public boolean validateToken(String token) {
        try {
            return !getAllClaimsFromToken(token).getExpiration().before(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }

        return null;
    }
}
