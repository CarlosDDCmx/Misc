package com.example.usermanagement.repository;

import com.example.usermanagement.model.OTP;
import com.example.usermanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
    Optional<OTP> findByUser(User user);
    Optional<OTP> findByCode(String code);
}