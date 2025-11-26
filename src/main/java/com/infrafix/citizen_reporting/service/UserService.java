package com.infrafix.citizen_reporting.service;

import com.infrafix.citizen_reporting.core.IService;
import com.infrafix.citizen_reporting.dto.UserResponseDTO;
import com.infrafix.citizen_reporting.model.Role;
import com.infrafix.citizen_reporting.model.User;
import com.infrafix.citizen_reporting.repo.RoleRepository;
import com.infrafix.citizen_reporting.repo.UserRepository;
import com.infrafix.citizen_reporting.security.BcryptCustom;
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
public class UserService implements IService<User> {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TransformPagination tp;

    @Autowired
    private BcryptCustom bcryptCustom;


    private static final String className = "UserService";

    @Override
    public ResponseEntity<Object> save(User user, HttpServletRequest request){
        if(user == null){
            return GlobalResponse.dataCreationFailed("IFUSFV001", request);
        }

        try {
            // Hash Password
            String hashedPassword = bcryptCustom.hash(user.getPassword());
            user.setPassword(hashedPassword);

            // Check if Role exist on DB
            if (user.getRole() == null) {
                Role citizenRole = roleRepository.findById(1L)
                        .orElseThrow(() -> new RuntimeException("Role with ID 1 (citizen) not found"));
                user.setRole(citizenRole);
            } else {
                // Optional: Overwrite Role ID if Provided
                Long roleId = user.getRole().getId();
                Role existingRole = roleRepository.findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role with ID " + roleId + " not found"));
                user.setRole(existingRole);
            }

            // Save User
            userRepository.save(user);

        } catch (Exception e){
            LoggingFile.logException(className,"save(User user, HttpServletRequest request) "
                    + RequestCapture.allRequest(request), e);
            return GlobalResponse.dataCreationFailed("IFUSFE001", request);
        }

        return GlobalResponse.dataCreation(request);
    }


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
            User nextUser = optionalUser.get();
            nextUser.setName(user.getName());
            nextUser.setAddress(user.getAddress());
            nextUser.setEmail(user.getEmail());
            nextUser.setPhoneNumber(user.getPhoneNumber());
            nextUser.setPostCode(user.getPostCode());
            nextUser.setModifiedBy(user.getModifiedBy());
            userRepository.save(nextUser);
        } catch (Exception e){
            LoggingFile.logException(className,"update(Long id, User user, HttpServletRequest request) " + RequestCapture.allRequest(request), e);
            return GlobalResponse.dataUpdateFailed("IFUSFV011", request);
        }
        return GlobalResponse.dataUpdate(request);
    }

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
            return GlobalResponse.errorOccurred("IFUSFE041", request);
        }
        return GlobalResponse.dataFound(data, request);
    }

    @Override
    public ResponseEntity<Object> findByParam (Pageable pageable, String column, String value, HttpServletRequest request){
        Page<User> page = null;
        List<UserResponseDTO> listDTO = null;
        Page<UserResponseDTO> pageRespo = null;
        Map<String, Object> data = null;
        try{
            switch(column){
                case "name": page = userRepository.findByNameContainsIgnoreCase(pageable, value); break;
                case "email": page = userRepository.findByEmailContainsIgnoreCase(pageable, value); break;
                case "address": page = userRepository.findByAddressContainsIgnoreCase(pageable, value); break;
                case "post_code": page = userRepository.findByPostCodeContainsIgnoreCase(pageable, value); break;
                default: page = userRepository.findAll(pageable);
            }
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
