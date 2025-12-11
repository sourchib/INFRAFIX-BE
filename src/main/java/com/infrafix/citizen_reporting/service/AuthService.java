package com.infrafix.citizen_reporting.service;

import com.infrafix.citizen_reporting.core.IAuthService;
import com.infrafix.citizen_reporting.dto.response.UserResponseDTO;
import com.infrafix.citizen_reporting.dto.validation.ValUserCreateDTO;
import com.infrafix.citizen_reporting.model.Role;
import com.infrafix.citizen_reporting.model.User;
import com.infrafix.citizen_reporting.repo.RoleRepository;
import com.infrafix.citizen_reporting.repo.UserRepository;
import com.infrafix.citizen_reporting.security.BcryptCustom;
import com.infrafix.citizen_reporting.security.CustomUserDetails;
import com.infrafix.citizen_reporting.security.JwtUtility;
import com.infrafix.citizen_reporting.util.GlobalFunction;
import com.infrafix.citizen_reporting.util.GlobalResponse;
import com.infrafix.citizen_reporting.util.LoggingFile;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.net.URI;
import org.springframework.http.HttpStatus;

/**
 * platform code : IF
 * module code : AS
 */

@Service
public class AuthService implements IAuthService<User> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BcryptCustom bcryptCustom;
    private final JwtUtility jwtUtility;
    private final EmailService emailService;
    private static final String className = "AuthService";

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, BcryptCustom bcryptCustom,
            JwtUtility jwtUtility, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bcryptCustom = bcryptCustom;
        this.jwtUtility = jwtUtility;
        this.emailService = emailService;
    }

    // LOGIN

    @Override
    public ResponseEntity<Object> login(User loginRequest, HttpServletRequest request) {
        try {
            if (loginRequest == null || loginRequest.getEmail() == null) {
                return GlobalResponse.dataNotFound("IFASV010", request);
            }

            // Email check
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Password check
            if (!bcryptCustom.verifyHash(loginRequest.getPassword(), user.getPassword())) {
                return GlobalResponse.incorrectPassword("IFASE020", request);
            }

            // Email verification check
            if (!user.getIsEmailVerified()) {
                return GlobalResponse.badRequest("Email not verified", request);
            }

            // Role check
            if (user.getRole() == null) {
                return GlobalResponse.dataNotFound("IFASE010", request);
            }

            // Build JWT claims
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole().getRole());
            claims.put("userId", user.getId());
            claims.put("isEmailVerified", user.getIsEmailVerified());

            String token = jwtUtility.doGenerateToken(claims, user.getEmail());

            // Data to return
            Map<String, Object> data = new HashMap<>();
            data.put("name", user.getName());
            data.put("token", token);
            data.put("role", user.getRole().getRole());
            data.put("email", user.getEmail());

            return GlobalResponse.dataFound(data, request);

        } catch (Exception e) {
            LoggingFile.logException(className,
                    "login(User loginRequest, HttpServletRequest request)", e);
            return GlobalResponse.dataNotFound("IFASE010", request);
        }
    }

    // LOAD USERNAME
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return new CustomUserDetails(user);
        } catch (Exception e) {
            LoggingFile.logException(className, "loadUserByUsername", e);
            throw e;
        }
    }

    // REGISTER
    @Override
    public ResponseEntity<UserResponseDTO> register(ValUserCreateDTO userCreateDTO) {
        try {
            // Check if user already exists
            if (userRepository.findByEmail(userCreateDTO.getEmail()).isPresent()) {
                return GlobalResponse.<UserResponseDTO>badRequest("IFASV030", null); // User with this email already
                                                                                     // exists
            }

            User user = new User();
            user.setName(userCreateDTO.getName());
            user.setEmail(userCreateDTO.getEmail());
            user.setPhoneNumber(userCreateDTO.getPhoneNumber());
            user.setAddress(userCreateDTO.getAddress());
            user.setPostCode(userCreateDTO.getPostCode());
            user.setPassword(bcryptCustom.hash(userCreateDTO.getPassword()));

            // Assign default role (e.g., "citizen")
            Role defaultRole = roleRepository.findByRole("citizen")
                    .orElseThrow(() -> new RuntimeException("Default role 'citizen' not found"));
            user.setRole(defaultRole);

            // Set createdBy (e.g., 0L for system/anonymous registration)
            user.setCreatedBy(0L);

            // Save user
            User savedUser = userRepository.save(user);

            // Generate email verification token
            Map<String, Object> claims = new HashMap<>();
            claims.put("name", savedUser.getName());
            claims.put("email", savedUser.getEmail());
            claims.put("userId", savedUser.getId());
            claims.put("purpose", "email_verification");
            claims.put("isEmailVerified", savedUser.getIsEmailVerified());
            String verificationToken = jwtUtility.doGenerateToken(claims, savedUser.getEmail());

            // Send verification email
            emailService.sendVerificationEmail(savedUser.getEmail(), verificationToken, savedUser.getName());

            // Prepare response data (excluding sensitive info like password)
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setId(savedUser.getId());
            userResponseDTO.setName(savedUser.getName());
            userResponseDTO.setEmail(savedUser.getEmail());
            userResponseDTO.setPhoneNumber(savedUser.getPhoneNumber());
            userResponseDTO.setAddress(savedUser.getAddress());
            userResponseDTO.setPostCode(savedUser.getPostCode());
            userResponseDTO.setRole(savedUser.getRole().getRole());
            userResponseDTO.setCreatedDate(savedUser.getCreatedDate());
            userResponseDTO.setCreatedBy(savedUser.getCreatedBy());

            return GlobalResponse.created(userResponseDTO, null); // Use null for HttpServletRequest as it's not needed
                                                                  // here

        } catch (Exception e) {
            LoggingFile.logException(className, "register(ValUserCreateDTO userCreateDTO)", e);
            return GlobalResponse.<UserResponseDTO>internalServerError("IFASV040", null);
        }
    }

    // VERIFY EMAIL
    public ResponseEntity<Object> verifyEmail(String token, HttpServletRequest request) {
        try {
            // Validate token
            if (!jwtUtility.validateToken(token)) {
                return GlobalResponse.badRequest("Invalid or expired token", request);
            }

            // Get claims
            Map<String, Object> claims = jwtUtility.mappingBodyToken(token);
            String purpose = (String) claims.get("purpose");
            if (!"email_verification".equals(purpose)) {
                return GlobalResponse.badRequest("Invalid token purpose", request);
            }

            Long userId = ((Number) claims.get("userId")).longValue();
            String email = (String) claims.get("email");
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Verify that the email in the token matches the user's email
            if (!email.equals(user.getEmail())) {
                return GlobalResponse.badRequest("Invalid token", request);
            }

            // Mark as verified if not already
            if (!user.getIsEmailVerified()) {
                user.setIsEmailVerified(true);
                userRepository.save(user);
            }

            // Generate NEW Access Token
            Map<String, Object> newClaims = new HashMap<>();
            newClaims.put("role", user.getRole().getRole());
            newClaims.put("userId", user.getId());
            newClaims.put("isEmailVerified", user.getIsEmailVerified());

            String accessToken = jwtUtility.doGenerateToken(newClaims, user.getEmail());

            // Redirect to Frontend
            String redirectUrl = "https://gentle-meringue-9613a6.netlify.app/verification-success?access_token="
                    + accessToken;

            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(redirectUrl))
                    .build();

        } catch (Exception e) {
            LoggingFile.logException(className, "verifyEmail(String token, HttpServletRequest request)", e);
            return GlobalResponse.internalServerError("Verification failed", request);
        }
    }
}
