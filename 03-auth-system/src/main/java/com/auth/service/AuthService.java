package com.auth.service;

import com.auth.dto.*;
import com.auth.exception.BadRequestException;
import com.auth.exception.TokenRefreshException;
import com.auth.model.RefreshToken;
import com.auth.model.Role;
import com.auth.model.User;
import com.auth.repository.RefreshTokenRepository;
import com.auth.repository.UserRepository;
import com.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    
    @Transactional
    public ApiResponse<JwtResponse> signup(SignupRequest signupRequest) {
        // Check if username exists
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        
        // Check if email exists
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new BadRequestException("Email is already in use");
        }
        
        // Create new user
        User user = User.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .enabled(true)
                .build();
        
        // Set roles
        Set<Role> roles = new HashSet<>();
        if (signupRequest.getRoles() != null && !signupRequest.getRoles().isEmpty()) {
            signupRequest.getRoles().forEach(roleName -> {
                try {
                    roles.add(Role.valueOf(roleName.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new BadRequestException("Invalid role: " + roleName);
                }
            });
        } else {
            roles.add(Role.USER); // Default role
        }
        user.setRoles(roles);
        
        userRepository.save(user);
        
        return ApiResponse.success("User registered successfully", null);
    }
    
    @Transactional
    public ApiResponse<JwtResponse> login(LoginRequest loginRequest) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        // Generate tokens
        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        
        // Get user from database
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new BadRequestException("User not found"));
        
        // Save or update refresh token
        saveRefreshToken(user, refreshToken);
        
        // Prepare response
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        
        JwtResponse jwtResponse = JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .build();
        
        return ApiResponse.success("Login successful", jwtResponse);
    }
    
    @Transactional
    public ApiResponse<JwtResponse> refreshToken(RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        
        // Validate refresh token
        if (!jwtUtil.validateToken(requestRefreshToken)) {
            throw new TokenRefreshException("Invalid refresh token");
        }
        
        // Find refresh token in database
        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestRefreshToken)
                .orElseThrow(() -> new TokenRefreshException("Refresh token not found"));
        
        // Check if refresh token is expired
        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenRefreshException("Refresh token has expired");
        }
        
        User user = refreshToken.getUser();
        
        // Load user details for token generation
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(user.getRoles().stream()
                                .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role.name()))
                                .collect(Collectors.toList()))
                        .build();
        
        // Generate new access token
        String newAccessToken = jwtUtil.generateToken(userDetails);
        
        Set<String> roles = user.getRoles().stream()
                .map(role -> "ROLE_" + role.name())
                .collect(Collectors.toSet());
        
        JwtResponse jwtResponse = JwtResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(requestRefreshToken)
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .build();
        
        return ApiResponse.success("Token refreshed successfully", jwtResponse);
    }
    
    @Transactional
    public ApiResponse<Void> logout(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User not found"));
        
        refreshTokenRepository.deleteByUser(user);
        
        return ApiResponse.success("Logout successful", null);
    }
    
    private void saveRefreshToken(User user, String token) {
        // Delete existing refresh token for user
        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);
        
        // Create new refresh token
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();
        
        refreshTokenRepository.save(refreshToken);
    }
}


