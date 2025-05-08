package com.example.EnvProfilesDB.controller;

import com.example.EnvProfilesDB.exception.UserNotFoundException;
import com.example.EnvProfilesDB.model.User;
import com.example.EnvProfilesDB.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        return userService.getAllUsers();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        logger.info("Adding new user: {}", user.getUsername());
        return userService.saveUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        logger.warn("Deleting user with ID: {}", id);
        userService.deleteUser(id).orElseThrow(() -> {
            logger.error("User not found with ID: {}", id);
            return new UserNotFoundException("User not found");
        });
    }
}