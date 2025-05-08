package com.example.usermanagement.service;

import org.springframework.stereotype.Service;
/*simple mockup
@Service
public class EmailService {
    public void sendPasswordResetEmail(String email, String token) {
        // Simulate email sending
        System.out.println("Sending password reset email to: " + email);
        System.out.println("Reset token: " + token);
    }
}*/


public class EmailService {
    public void sendPasswordResetEmail(String email, String token) {
        // Implementation for sending email
        System.out.println("Password reset link: http://<app-url-service>/reset-password?token=" + token);
    }

    public void sendOTPEmail(String email, String code) {
        // Implementation for sending OTP
        System.out.println("Your OTP code: " + code);
    }
}