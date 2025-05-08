# UsersLog - Spring Boot Application with Profile-Specific Logging

This project demonstrates how to implement environment-specific logging with Spring profiles in a Spring Boot application. The application serves as a basic REST API for managing users with different logging configurations for development, testing, and production environments.

## Features

- REST API for user management (get, add, delete users)
- Environment-specific configurations using Spring profiles (dev, test, prod)
- Profile-specific logging levels and outputs
- Structured JSON logging for integration with ELK Stack or Splunk
- H2 in-memory database with profile-specific configurations

## Project Structure

```
UsersLog/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── UsersLog/
│   │   │               ├── UsersLogApplication.java
│   │   │               ├── controller/
│   │   │               │   ├── ProfileController.java
│   │   │               │   └── UserController.java
│   │   │               ├── model/
│   │   │               │   └── User.java
│   │   │               ├── repository/
│   │   │               │   └── UserRepository.java
│   │   │               └── service/
│   │   │                   └── UserService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-test.properties
│   │       ├── application-prod.properties
│   │       └── logback-spring.xml
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── UsersLog/
│                       └── UsersLogApplicationTests.java
├── logs/        # Location for log files in production
├── pom.xml
└── README.md
```

## Setup and Running the Application

### Prerequisites

- Java 21
- Maven
- IntelliJ IDEA (recommended) or any Java IDE
- Git

### Clone the Repository

```bash
git clone https://github.com/your-username/UsersLog.git
cd UsersLog
```

### Build the Project

```bash
mvn clean install
```

### Run the Application with Different Profiles

#### Development Profile (Default)

```bash
mvn spring-boot:run
```

or explicitly:

```bash
mvn spring-boot:run -Dspring.profiles.active=dev
```

This will:
- Start the application on port 8081
- Enable DEBUG-level logging
- Output logs to the console
- Enable H2 console at http://localhost:8081/h2-console

#### Test Profile

```bash
mvn spring-boot:run -Dspring.profiles.active=test
```

This will:
- Start the application on port 8082
- Enable INFO-level logging
- Output logs to the console
- Enable H2 console at http://localhost:8082/h2-console

#### Production Profile

```bash
mvn spring-boot:run -Dspring.profiles.active=prod
```

This will:
- Start the application on port 8083
- Enable only WARN and ERROR-level logging
- Output logs to files in the `logs` directory
- Disable H2 console for security

### Switching Profiles at Runtime

The application supports changing profiles at runtime using the Actuator endpoint:

```bash
curl -X POST http://localhost:8081/actuator/env -H "Content-Type: application/json" -d '{"name":"spring.profiles.active", "value":"test"}'
curl -X POST http://localhost:8081/actuator/refresh
```

## API Endpoints

### Get All Users

```
GET /users
```

Example:
```bash
curl -X GET http://localhost:8081/users
```

### Add a New User

```
POST /users/{json data}
```

Example:
```bash
curl -X POST http://localhost:8081/users -H "Content-Type: application/json" -d '{"username":"newuser","email":"newuser@example.com","role":"USER"}'
```

### Delete User by ID

```
DELETE /users/{id}
```

Example:
```bash
curl -X DELETE http://localhost:8081/users/3
```

## Profile-Specific Logging Configurations

### Development Profile (`dev`)
- **Log Levels**: DEBUG for application code, INFO for Spring and Hibernate
- **Log Output**: Console
- **Purpose**: Detailed logging for development and debugging

### Test Profile (`test`)
- **Log Levels**: INFO for application code, WARN for Spring and Hibernate
- **Log Output**: Console
- **Purpose**: Minimal logging for testing purposes

### Production Profile (`prod`)
- **Log Levels**: WARN and ERROR only
- **Log Output**: Files (`logs/UsersLog.log` and `logs/UsersLog-json.log` for ELK integration)
- **Purpose**: Performance-optimized logging, reduced verbosity

## ELK Stack / Splunk Integration

The application is configured to produce JSON-formatted logs for integration with log analytics platforms:

1. **JSON Log Format**: Logs are written to `logs/UsersLog-json.log` in JSON format.
2. **Structured Logging**: Each log entry includes:
   - Application name
   - Active profile
   - Timestamp
   - Thread
   - Log level
   - Logger name
   - Message

To integrate with ELK Stack:
1. Configure Filebeat to collect logs from the `logs` directory
2. Send logs to Logstash for processing
3. Store in Elasticsearch
4. Visualize in Kibana

To integrate with Splunk:
1. Configure Splunk Universal Forwarder to monitor the `logs` directory
2. Define source type as JSON
3. Index logs in Splunk
4. Create dashboards and alerts

## Testing the Profile-Specific Logging

### Testing Dev Profile
```bash
# Start with dev profile (DEBUG level enabled)
mvn spring-boot:run -Dspring.profiles.active=dev

# Make a request and observe DEBUG logs in console
curl -X GET http://localhost:8081/users
```

### Testing Test Profile
```bash
# Start with test profile (only INFO level and above)
mvn spring-boot:run -Dspring.profiles.active=test

# Make a request and observe only INFO logs in console (no DEBUG)
curl -X GET http://localhost:8082/users
```

### Testing Prod Profile
```bash
# Start with prod profile (only WARN and ERROR levels)
mvn spring-boot:run -Dspring.profiles.active=prod

# Make a regular request (no logs for successful operations)
curl -X GET http://localhost:8083/users

# Make a request that triggers a warning (observe log file)
curl -X DELETE http://localhost:8083/users/999
```

## Known Issues and Solutions

### Reserved SQL Keywords Issue

If you encounter errors like this:

```
Error executing DDL "drop table if exists user cascade" via JDBC 
[Syntax error in SQL statement "drop table if exists [*]user cascade"; expected "identifier";]
```

This is because "user" is a reserved keyword in many SQL databases, including H2. This application addresses this in two ways:

1. Using `@Table(name = "app_user")` annotation in the User entity to specify a non-reserved table name
2. Adding `spring.jpa.properties.hibernate.globally_quoted_identifiers=true` to automatically quote identifiers

These solutions prevent conflicts with reserved SQL keywords in the database schema.

### JSON format not vaild
If you encounter errors containing this:

```
"status":400,"error":"Bad Request","path":"/actuator/env"}curl: (3) unmatched close brace/bracket in URL position
```

It means the symbol ' is not recognized in your current command line or terminal and instead should use escape characters such as "" like this for example:

```bash
curl -X POST http://localhost:8081/actuator/env -H "Content-Type: application/json" -d "{\"name\":\"spring.profiles.active\", \"value\":\"test\"}"
```