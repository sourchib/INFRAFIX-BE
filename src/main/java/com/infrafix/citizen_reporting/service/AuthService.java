package com.infrafix.citizen_reporting.service;

import com.infrafix.citizen_reporting.model.User;
import com.infrafix.citizen_reporting.repo.UserRepository;
import com.infrafix.citizen_reporting.security.BcryptCustom;
import com.infrafix.citizen_reporting.security.CustomUserDetails;
import com.infrafix.citizen_reporting.security.JwtUtility;
import com.infrafix.citizen_reporting.util.GlobalResponse;
import com.infrafix.citizen_reporting.util.LoggingFile;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BcryptCustom bcryptCustom;
    private final JwtUtility jwtUtility;
    private static final String className = "AuthService";

    public AuthService(UserRepository userRepository, BcryptCustom bcryptCustom, JwtUtility jwtUtility) {
        this.userRepository = userRepository;
        this.bcryptCustom = bcryptCustom;
        this.jwtUtility = jwtUtility;
    }

    public ResponseEntity<Object> login(User loginRequest, HttpServletRequest request) {
        try {
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Verify password
            if (!bcryptCustom.verifyHash(loginRequest.getPassword(), user.getPassword())) {
                return GlobalResponse.dataCreationFailed("AUTHFE001", request);
            }

            // Check if user is admin
            if (user.getRole() == null || !"ADMIN".equalsIgnoreCase(user.getRole().getRole())) {
                return GlobalResponse.notAdmin("AUTHFE002", request);
            }

            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole().getRole());

            String token = jwtUtility.doGenerateToken(claims, user.getEmail());

            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("role", user.getRole().getRole());

            return GlobalResponse.dataFound(data, request);

        } catch (Exception e) {
            LoggingFile.logException(className, "login", e);
            return GlobalResponse.dataCreationFailed("AUTHFE001", request);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }

    public boolean checkPassword(String raw, String hashed) {
        return bcryptCustom.verifyHash(raw, hashed);
    }
}
