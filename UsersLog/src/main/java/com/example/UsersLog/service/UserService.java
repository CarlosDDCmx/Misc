package com.example.UsersLog.service;

import com.example.UsersLog.model.User;
import com.example.UsersLog.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        
        // Add some sample users for testing
        if (userRepository.count() == 0) {
            logger.debug("Initializing sample users for testing");
            userRepository.save(new User("admin", "admin@example.com", "ADMIN"));
            userRepository.save(new User("user1", "user1@example.com", "USER"));
            userRepository.save(new User("user2", "user2@example.com", "USER"));
            logger.info("Sample users initialized successfully");
        }
    }
    
    public List<User> getAllUsers() {
        logger.debug("Fetching all users from the database");
        List<User> users = userRepository.findAll();
        logger.info("Retrieved {} users from database", users.size());
        return users;
    }
    
    public User addUser(User user) {
        if (user == null) {
            logger.error("Cannot add null user");
            throw new IllegalArgumentException("User cannot be null");
        }
        
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            logger.warn("Attempted to add user with empty username");
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        logger.debug("Adding new user: {}", user.getUsername());
        User savedUser = userRepository.save(user);
        logger.info("User added successfully with ID: {}", savedUser.getId());
        return savedUser;
    }
    
    public boolean deleteUser(Long id) {
        logger.debug("Attempting to delete user with ID: {}", id);
        
        if (id == null) {
            logger.error("Cannot delete user with null ID");
            throw new IllegalArgumentException("User ID cannot be null");
        }
        
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            logger.info("User with ID {} deleted successfully", id);
            return true;
        } else {
            logger.warn("User with ID {} not found for deletion", id);
            return false;
        }
    }
    
    public Optional<User> getUserById(Long id) {
        logger.debug("Fetching user with ID: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            logger.info("User found with ID: {}", id);
        } else {
            logger.warn("User not found with ID: {}", id);
        }
        return user;
    }
}
