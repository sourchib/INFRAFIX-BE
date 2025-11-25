package com.infrafix.citizen_reporting.service;

import com.infrafix.citizen_reporting.core.IService;
import com.infrafix.citizen_reporting.dto.UserResponseDTO;
import com.infrafix.citizen_reporting.model.User;
import com.infrafix.citizen_reporting.repo.UserRepository;
import com.infrafix.citizen_reporting.util.GlobalResponse;
import com.infrafix.citizen_reporting.util.LoggingFile;
import com.infrafix.citizen_reporting.util.RequestCapture;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
//import org.thymeleaf.spring6.SpringTemplateEngine;

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
    private TransformPagination tp;

    private ModelMapper modelMapper = new ModelMapper();
    private StringBuilder sBuild =  new StringBuilder();


    private static final String className = "UserService";

    @Override
    public ResponseEntity<Object> save(User user, HttpServletRequest request){
        if(user == null){
            return GlobalResponse.dataCreationFailed("IFUSFV001", request);
        }
        try{
            userRepository.save(user);
        } catch (Exception e){
            LoggingFile.logException(className,"save (User user, HttpServletRequest request) " + RequestCapture.allRequest(request),e);
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
            data =
        }
    }

    @Override
    public ResponseEntity<Object> findByParam (Pageable pageable, String column, String value, HttpServletRequest request){

    }
}


public UserResponseDTO entityToDTO(User user){
    UserResponseDTO userResponseDTO = new UserResponseDTO();
    userResponseDTO.setAddress(user.getAddress());
    userResponseDTO.setEmail(user.getEmail());
    userResponseDTO.setName(user.getName());
    userResponseDTO.setPhoneNumber(user.getPhoneNumber());
    userResponseDTO.setPostCode(user.getPostCode());
    return userResponseDTO;
}