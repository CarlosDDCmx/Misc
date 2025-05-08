package com.example.usermanagement.controller;

import com.example.usermanagement.model.User;
import com.example.usermanagement.security.UserDetailsImpl;
import com.example.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "User profile and administration endpoints")
public class UserController {
    @Autowired
    private UserService userService;

    // Admin-only endpoint
    @Operation(summary = "Get all users", description = "Admin-only endpoint")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "List of users retrieved")
    @ApiResponse(responseCode = "403", description = "Forbidden (admin access required)")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // User-specific or admin access
    @Operation(summary = "Get user by ID", description = "User can access own profile, admin can access any")
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(name = "userId", description = "ID of user to retrieve")
    @ApiResponse(responseCode = "200", description = "User details retrieved")
    @ApiResponse(responseCode = "403", description = "Forbidden (not owner or admin)")
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == principal.user.id")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    // Current user profile
    @Operation(summary = "Get user by ID", description = "User can access own profile, admin can access any")
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(name = "userId", description = "ID of user to retrieve")
    @ApiResponse(responseCode = "200", description = "User details retrieved")
    @ApiResponse(responseCode = "403", description = "Forbidden (not owner or admin)")
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return ResponseEntity.ok(userDetails.getUser());
    }

    // User can update own profile
    @Operation(summary = "Get user by ID", description = "User can access own profile, admin can access any")
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(name = "userId", description = "ID of user to retrieve")
    @ApiResponse(responseCode = "200", description = "User details retrieved")
    @ApiResponse(responseCode = "403", description = "Forbidden (not owner or admin)")
    @PutMapping("/{userId}")
    @PreAuthorize("#userId == principal.user.id")
    public ResponseEntity<User> updateUser(
            @PathVariable Long userId,
            @RequestBody User updatedUser) {
        return ResponseEntity.ok(userService.updateUser(userId, updatedUser));
    }

    // Admin-only deletion
    @Operation(summary = "Get user by ID", description = "User can access own profile, admin can access any")
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(name = "userId", description = "ID of user to retrieve")
    @ApiResponse(responseCode = "200", description = "User details retrieved")
    @ApiResponse(responseCode = "403", description = "Forbidden (not owner or admin)")
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}