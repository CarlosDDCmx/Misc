package com.example.EnvProfilesDB.repository;

import com.example.EnvProfilesDB.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
