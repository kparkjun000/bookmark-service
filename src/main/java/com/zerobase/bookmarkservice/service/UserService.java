package com.zerobase.bookmarkservice.service;

import com.zerobase.bookmarkservice.dto.UserDto;
import com.zerobase.bookmarkservice.entity.User;
import com.zerobase.bookmarkservice.exception.CustomException;
import com.zerobase.bookmarkservice.exception.ErrorCode;
import com.zerobase.bookmarkservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional
    public UserDto.Response createUser(UserDto.Create request) {
        log.info("Creating user with email: {}", request.getEmail());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }
        
        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .build();
        
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
        
        return UserDto.Response.from(savedUser);
    }
    
    public UserDto.Response getUser(Long userId) {
        log.info("Getting user with ID: {}", userId);
        User user = findUserById(userId);
        return UserDto.Response.from(user);
    }
    
    public List<UserDto.Response> getAllUsers() {
        log.info("Getting all users");
        return userRepository.findAll().stream()
                .map(UserDto.Response::from)
                .collect(Collectors.toList());
    }
    
    public UserDto.Response getUserByEmail(String email) {
        log.info("Getting user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserDto.Response.from(user);
    }
    
    @Transactional
    public void deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        User user = findUserById(userId);
        userRepository.delete(user);
        log.info("User deleted successfully with ID: {}", userId);
    }
    
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}

