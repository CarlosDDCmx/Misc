package com.example.UsersLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.io.File;
import java.util.Arrays;

@SpringBootApplication
public class UsersLogApplication {

    private static final Logger logger = LoggerFactory.getLogger(UsersLogApplication.class);

    public static void main(String[] args) {
        createLogsDirectoryIfNeeded();

        ConfigurableApplicationContext context = SpringApplication.run(UsersLogApplication.class, args);

        Environment env = context.getEnvironment();
        String activeProfiles = Arrays.toString(env.getActiveProfiles());
        String serverPort = env.getProperty("server.port");

        logger.debug("Application started with DEBUG level logging");
        logger.info("Application started with active profiles: {}", activeProfiles);
        logger.info("Server is running on port: {}", serverPort);

        if (activeProfiles.contains("dev")) {
            logger.warn("This is a sample WARN message during startup (for demo only)");
            logger.error("This is a sample ERROR message during startup (for demo only)");
        }
    }

    private static void createLogsDirectoryIfNeeded() {
        File logsDir = new File("logs");
        if (!logsDir.exists()) {
            boolean created = logsDir.mkdirs();
            if (created) {
                System.out.println("Created logs directory");
            } else {
                System.err.println("Failed to create logs directory");
            }
        }
    }
}