package com.example.usermanagement.service;

import com.example.usermanagement.model.OTP;
import com.example.usermanagement.model.User;
import com.example.usermanagement.repository.OTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Random;

@Service
public class OTPService {
    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private EmailService emailService;

    public void generateAndSendOTP(User user) {
        String code = String.format("%06d", new Random().nextInt(999999));
        OTP otp = new OTP();
        otp.setCode(code);
        otp.setUser(user);
        otp.setExpiryDate(Instant.now().plusMillis(300000)); // 5 minutes
        otpRepository.save(otp);
        emailService.sendOTPEmail(user.getEmail(), code);
    }

    public boolean validateOTP(User user, String code) {
        OTP otp = otpRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("OTP not found"));
        if (otp.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("OTP expired");
        }
        return otp.getCode().equals(code);
    }
}