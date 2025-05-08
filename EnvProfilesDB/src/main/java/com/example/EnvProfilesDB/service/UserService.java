package com.example.EnvProfilesDB.service;

import com.example.EnvProfilesDB.model.User;
import com.example.EnvProfilesDB.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        logger.debug("Fetching all users from database");
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        logger.info("Saving user: {}", user.getUsername());
        return userRepository.save(user);
    }

    public Optional<User> deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            logger.warn("Deleting user ID: {}", id);
            userRepository.deleteById(id);
        }
        return user;
    }
}
