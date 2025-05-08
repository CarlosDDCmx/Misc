package com.example.UsersLog;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UsersLogApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(UsersLogApplicationTests.class);

    @Test
    void contextLoads() {
        logger.debug("This is a DEBUG message in tests - should not appear in test profile");
        logger.info("This is an INFO message in tests - should appear in test profile");
        logger.warn("This is a WARN message in tests - should appear in test profile");
        logger.error("This is an ERROR message in tests - should appear in test profile");
    }

}
