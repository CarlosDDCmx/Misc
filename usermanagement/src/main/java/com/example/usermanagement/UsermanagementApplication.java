package com.example.usermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.usermanagement")
public class UsermanagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(UsermanagementApplication.class, args);
	}
}
