package com.infrafix.citizen_reporting.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtility {

    private SecretKey key;

    @PostConstruct
    public void init() {
        // Generate a strong HS512 key (512+ bits)
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Map<String, Object> mappingBodyToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", claims.get("id"));
        map.put("username", claims.getSubject());
        map.put("noHp", claims.get("hp"));
        map.put("namaLengkap", claims.get("naleng"));
        map.put("email", claims.get("em"));
        map.put("role", claims.get("role"));
        return map;
    }

    public String doGenerateToken(Map<String, Object> claims, String subject) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1800000)) // e.g., 30 min
                .signWith(key)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(getAllClaimsFromToken(token));
    }

    public boolean validateToken(String token) {
        try {
            return !getAllClaimsFromToken(token).getExpiration().before(new Date());
        } catch (JwtException e) {
            return false;
        }
    }
}
