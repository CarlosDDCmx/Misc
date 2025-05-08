package com.example.UsersLog.controller;

import com.example.UsersLog.model.User;
import com.example.UsersLog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        logger.debug("Received request to get all users");
        List<User> users = userService.getAllUsers();
        logger.info("Returning {} users", users.size());
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        logger.debug("Received request to get user with ID: {}", id);
        
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            logger.info("Found user with ID: {}", id);
            return ResponseEntity.ok(user.get());
        } else {
            logger.warn("User with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        logger.debug("Received request to add new user: {}", user);
        
        try {
            User addedUser = userService.addUser(user);
            logger.info("User added successfully with ID: {}", addedUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(addedUser);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to add user: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.debug("Received request to delete user with ID: {}", id);
        
        try {
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                logger.info("User with ID {} deleted successfully", id);
                return ResponseEntity.noContent().build();
            } else {
                logger.warn("User with ID {} not found for deletion", id);
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            logger.error("Failed to delete user: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
