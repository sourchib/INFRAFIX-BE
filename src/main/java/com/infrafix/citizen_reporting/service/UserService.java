package com.infrafix.citizen_reporting.service;

import com.infrafix.citizen_reporting.core.IUserService;
import com.infrafix.citizen_reporting.dto.response.UserResponseDTO;
import com.infrafix.citizen_reporting.dto.validation.ValUserCreateDTO;
import com.infrafix.citizen_reporting.model.Role;
import com.infrafix.citizen_reporting.model.User;
import com.infrafix.citizen_reporting.repo.RoleRepository;
import com.infrafix.citizen_reporting.repo.UserRepository;
import com.infrafix.citizen_reporting.security.BcryptCustom;
import com.infrafix.citizen_reporting.security.JwtContextUtil;
import com.infrafix.citizen_reporting.util.GlobalResponse;
import com.infrafix.citizen_reporting.util.LoggingFile;
import com.infrafix.citizen_reporting.util.RequestCapture;
import com.infrafix.citizen_reporting.util.TransformPagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * platform code : IF
 * module code : US
 */

@Service
@Transactional
public class UserService implements IUserService<ValUserCreateDTO, User> {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TransformPagination tp;

    @Autowired
    private BcryptCustom bcryptCustom;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private JwtContextUtil jwtContextUtil;

    private static final String className = "UserService";


    // SAVE

    @Override
    public ResponseEntity<Object> createCitizen(ValUserCreateDTO dto, HttpServletRequest request) {

        if (dto == null) {
            return GlobalResponse.dataCreationFailed("IFUSFV001", request);
        }
        try {
            Long creatorId = 0L; // 0 = New User

            User user = new User();
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setAddress(dto.getAddress());
            user.setPhoneNumber(dto.getPhoneNumber());
            user.setPostCode(dto.getPostCode());
            user.setCreatedBy(creatorId);

            // Hash Password
            String hashedPassword = bcryptCustom.hash(dto.getPassword());
            user.setPassword(hashedPassword);

            // Always assign CITIZEN role
            Role citizenRole = roleRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Citizen role not found"));
            user.setRole(citizenRole);

            // Save Citizen
            User savedUser = userRepository.save(user);
            UserResponseDTO userResponseDTO = entityToDTO(savedUser);
            return GlobalResponse.created(userResponseDTO, request);

        } catch (Exception e) {
            LoggingFile.logException(
                    className,
                    "createCitizen(ValUserCreateDTO dto, HttpServletRequest request) " +
                            RequestCapture.allRequest(request),
                    e
            );
            return GlobalResponse.dataCreationFailed("IFUSFE001", request);
        }
    }



    // UPDATE

    @Override
    public ResponseEntity<Object> update(Long id, User user, HttpServletRequest request){
        if(user==null){
            return GlobalResponse.dataUpdateFailed("IFUSFV011", request);
        }
        try{
            Optional<User> optionalUser = userRepository.findById(id);
            if(optionalUser.isEmpty()){
                return GlobalResponse.dataNotFound("IFUSFV041", request);
            }

            Long modifierId = jwtContextUtil.getCurrentUserId(request);

            User nextUser = optionalUser.get();
            nextUser.setName(user.getName());
            nextUser.setAddress(user.getAddress());
            nextUser.setEmail(user.getEmail());
            nextUser.setPhoneNumber(user.getPhoneNumber());
            nextUser.setPostCode(user.getPostCode());
            nextUser.setModifiedBy(modifierId);
            userRepository.save(nextUser);
        } catch (Exception e){
            LoggingFile.logException(className,"update(Long id, User user, HttpServletRequest request) " + RequestCapture.allRequest(request), e);
            return GlobalResponse.dataUpdateFailed("IFUSFV011", request);
        }
        return GlobalResponse.dataUpdate(request);
    }


    // DELETE

    @Override
    public ResponseEntity<Object> delete (Long id, HttpServletRequest request){
        if(id == null){
            return GlobalResponse.dataNotFound("IFUSFV041", request);
        }
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isEmpty()) {
                return GlobalResponse.dataNotFound("IFUSFV041", request);
            }
            userRepository.deleteById(id);
        } catch (Exception e){
            LoggingFile.logException(className,"delete(Long id, HttpServletRequest request) " + RequestCapture.allRequest(request), e);
            return GlobalResponse.dataNotFound("IFUSFV041", request);
        }
        return GlobalResponse.dataDeletion(request);
    }


    // FIND BY ID

    @Override
    public ResponseEntity<Object> findById (Long id, HttpServletRequest request){
        User nextUser = null;
        UserResponseDTO userResponseDTO = null;
        if(id == null){
            return GlobalResponse.dataNotFound("IFUSFV041", request);
        }
        try{
            Optional<User> optionalUser = userRepository.findById(id);
            if(optionalUser.isEmpty()){
                return GlobalResponse.dataNotFound("IFUSFV041", request);
            }
            nextUser = optionalUser.get();
            userResponseDTO = entityToDTO(nextUser);
        } catch (Exception e){
            LoggingFile.logException(className,"findById(Long id, HttpServletRequest request) " + RequestCapture.allRequest(request), e);
            return GlobalResponse.dataNotFound("IFUSFV041", request);
        }
        return GlobalResponse.dataFound(userResponseDTO, request);
    }


    // FIND ALL

    @Override
    public ResponseEntity<Object> findAll (Pageable pageable, HttpServletRequest request){
        Page<User> page = null;
        List<UserResponseDTO> listDTO = null;
        Page<UserResponseDTO> pageRespo = null;
        Map<String, Object> data = null;
        try{
            page = userRepository.findAll(pageable);
            if(page.isEmpty()){
                return GlobalResponse.dataNotFound("IFUSFV041", request);
            }
            listDTO = entityToDTO(page.getContent());
            data = tp.transformPagination(listDTO, page, "id", "");
        } catch (Exception e){
            LoggingFile.logException(className,"findAll(Pageable pageable, HttpServletRequest request) " + RequestCapture.allRequest(request), e);
            return GlobalResponse.internalServerError("IFUSFE041", request);
        }
        return GlobalResponse.dataFound(data, request);
    }


    // FIND BY PARAM

    @Override
    public ResponseEntity<Object> findByParam (Pageable pageable, String column, String value, HttpServletRequest request){
        Page<User> page = null;
        List<UserResponseDTO> listDTO = null;
        Page<UserResponseDTO> pageRespo = null;
        Map<String, Object> data = null;
        try{
            page = switch (column) {
                case "name" -> userRepository.findByNameContainsIgnoreCase(pageable, value);
                case "email" -> userRepository.findByEmailContainsIgnoreCase(pageable, value);
                case "address" -> userRepository.findByAddressContainsIgnoreCase(pageable, value);
                case "post_code" -> userRepository.findByPostCodeContainsIgnoreCase(pageable, value);
                default -> userRepository.findAll(pageable);
            };
            if(page.isEmpty()){
                return GlobalResponse.dataNotFound("IFUSFV051", request);
            }
            listDTO = entityToDTO(page.getContent());
            data = tp.transformPagination(listDTO, page, column, value);
        }catch (Exception e){
            LoggingFile.logException(className, "findByParam(Pageable pageable, String column, String value, HttpServletRequest request) " + RequestCapture.allRequest(request), e);
            return GlobalResponse.dataNotFound("IFUSFV051", request);
        }
        return GlobalResponse.dataFound(data, request);
    }


    // CREATE TECHNICIAN

    @Override
    public ResponseEntity<Object> createTechnician(ValUserCreateDTO dto, HttpServletRequest request) {

        try {
            if (dto == null) {
                return GlobalResponse.dataCreationFailed("IFUSTC001", request);
            }

            Long creatorId = jwtContextUtil.getCurrentUserId(request);

            // Validate duplicate email
            if (userRepository.existsByEmail(dto.getEmail())) {
                return GlobalResponse.dataCreationFailed("IFUSTC002", request);
            }

            // Get Technician Role
            Role technicianRole = roleRepo.findByRole("Technician")
                    .orElseThrow(() -> new RuntimeException("Technician Role missing"));

            // Create user
            User user = new User();
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setPassword(bcryptCustom.hash(dto.getPassword()));
            user.setPhoneNumber(dto.getPhoneNumber());
            user.setAddress(dto.getAddress());
            user.setPostCode(dto.getPostCode());
            user.setRole(technicianRole);
            user.setCreatedBy(creatorId);

            User saved = userRepository.save(user);

            // Convert to DTO
            UserResponseDTO responseDTO = entityToDTO(saved);
            return GlobalResponse.created(responseDTO, request);


        } catch (Exception e) {
            LoggingFile.logException(
                    className,
                    "createTechnician(ValUserCreateDTO dto, HttpServletRequest request) "
                            + RequestCapture.allRequest(request),
                    e
            );
            return GlobalResponse.dataCreationFailed("IFUSTC999", request);
        }
    }


    // CREATE ADMIN

    @Override
    public ResponseEntity<Object> createAdmin(ValUserCreateDTO dto, HttpServletRequest request) {
        if (dto == null) {
            return GlobalResponse.dataCreationFailed("IFUSFV001", request);
        }

        try {
            // Get the currently logged-in admin ID
            Long creatorId = jwtContextUtil.getCurrentUserId(request);

            // Check duplicate email
            if (userRepository.existsByEmail(dto.getEmail())) {
                return GlobalResponse.dataCreationFailed("IFUSFV002", request);
            }

            User user = new User();
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setAddress(dto.getAddress());
            user.setPhoneNumber(dto.getPhoneNumber());
            user.setPostCode(dto.getPostCode());
            user.setCreatedBy(creatorId);

            // Hash password
            user.setPassword(bcryptCustom.hash(dto.getPassword()));

            // Assign ADMIN role
            Role adminRole = roleRepository.findById(2L)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
            user.setRole(adminRole);

            User savedUser = userRepository.save(user);

            // Map to DTO
            UserResponseDTO responseDTO = entityToDTO(savedUser);

            return GlobalResponse.created(responseDTO, request);

        } catch (Exception e) {
            LoggingFile.logException(
                    className,
                    "createAdmin(ValUserCreateDTO dto, HttpServletRequest request) " +
                            RequestCapture.allRequest(request),
                    e
            );
            return GlobalResponse.dataCreationFailed("IFUSFE001", request);
        }
    }


    // DTO

    public UserResponseDTO entityToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setPostCode(user.getPostCode());

        // Convert Role entity to String
        if (user.getRole() != null) {
            dto.setRole(user.getRole().getRole());
        }
        return dto;
    }

    public List<UserResponseDTO> entityToDTO(List<User> users) {
        List<UserResponseDTO> listUserDTO = new ArrayList<>();
        for (User u : users) {
            UserResponseDTO dto = new UserResponseDTO();

            dto.setId(u.getId());
            dto.setName(u.getName());
            dto.setEmail(u.getEmail());
            dto.setPhoneNumber(u.getPhoneNumber());
            dto.setAddress(u.getAddress());
            dto.setPostCode(u.getPostCode());

            if (u.getRole() != null) {
                dto.setRole(u.getRole().getRole());
            }
            listUserDTO.add(dto);
        }
        return listUserDTO;
    }
}
