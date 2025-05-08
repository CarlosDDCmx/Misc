package com.example.usermanagement.controller;

import com.example.usermanagement.dto.*;
import com.example.usermanagement.exception.TokenRefreshException;
import com.example.usermanagement.model.RefreshToken;
import com.example.usermanagement.security.JwtUtils;
import com.example.usermanagement.security.UserDetailsImpl;
import com.example.usermanagement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final PasswordResetService passwordResetService;
    private final OTPService otpService;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthController(AuthService authService,
                          PasswordResetService passwordResetService,
                          OTPService otpService,
                          JwtUtils jwtUtils,
                          RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
        this.otpService = otpService;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        authService.registerUser(
                registrationRequest.getUsername(),
                registrationRequest.getEmail(),
                registrationRequest.getPassword()
        );
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateUser(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwtResponse.getAccessToken())
                .body(jwtResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String newAccessToken = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new JwtResponse(newAccessToken, refreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException("Invalid refresh token"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        String jwt = parseJwt(request);
        if (jwt != null) {
            jwtUtils.invalidateToken(jwt);
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        refreshTokenService.deleteByUserId(userDetails.getUser().getId());

        return ResponseEntity.ok("Logout successful");
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}