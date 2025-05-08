package com.example.UsersLog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RefreshScope
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private Environment environment;

    @Autowired
    private ConfigurableEnvironment configurableEnvironment;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private ContextRefresher contextRefresher;

    @PostMapping("/switch-profile")
    public ResponseEntity<Map<String, Object>> switchProfile(@RequestBody Map<String, String> request) {
        String newProfile = request.get("profile");

        if (newProfile == null || newProfile.isEmpty()) {
            logger.warn("Attempt to switch to empty profile");
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Profile name cannot be empty");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Validate profile is supported
        if (!Arrays.asList("dev", "test", "prod").contains(newProfile)) {
            logger.warn("Attempt to switch to unsupported profile: {}", newProfile);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unsupported profile: " + newProfile + ". Must be one of: dev, test, prod");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        logger.info("Attempting to switch active profile to: {}", newProfile);

        try {
            // Set spring.profiles.active property with the new profile
            Map<String, Object> propertyMap = new HashMap<>();
            propertyMap.put("spring.profiles.active", newProfile);

            // Add or update the property source
            MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
            MapPropertySource propertySource = new MapPropertySource("dynamic-profile", propertyMap);

            if (propertySources.contains("dynamic-profile")) {
                propertySources.replace("dynamic-profile", propertySource);
            } else {
                propertySources.addFirst(propertySource);
            }

            // Refresh context if ContextRefresher is available
            if (contextRefresher != null) {
                contextRefresher.refresh();
                logger.info("Context refreshed with new profile: {}", newProfile);
            } else {
                logger.warn("ContextRefresher not available, changes may not be fully applied");
            }

            logger.info("Successfully switched active profile to: {}", newProfile);

            // Return current environment information
            Map<String, Object> response = new HashMap<>();
            response.put("activeProfiles", Arrays.asList(environment.getActiveProfiles()));
            response.put("currentProfile", newProfile);
            response.put("message", "Profile switched successfully to " + newProfile);

            // Require restart when necessary
            response.put("note", "You may need to restart the application for all profile settings to take effect");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error switching profile: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to switch profile: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile() {
        Map<String, Object> response = new HashMap<>();
        response.put("activeProfiles", Arrays.asList(environment.getActiveProfiles()));
        response.put("defaultProfiles", Arrays.asList(environment.getDefaultProfiles()));
        response.put("currentServerPort", environment.getProperty("server.port"));
        response.put("environment", environment.getProperty("app.environment"));
        return ResponseEntity.ok(response);
    }
}