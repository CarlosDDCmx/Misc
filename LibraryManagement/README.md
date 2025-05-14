# Library Management System API

A Spring Boot REST API for managing books in a library, with MySQL integration and Swagger documentation.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Setup & Configuration](#setup--configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Sample Requests](#sample-requests)

---

## Prerequisites
- Java 21
- MySQL 8.x
- Maven 3.9+
- Postman/cURL (for testing)

---

## Setup & Configuration

1. **Create MySQL Database**
   ```sql
   CREATE DATABASE library_management;
   ```

2. **Update Application Properties**
   Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/library_management
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Build the Project**
   ```bash
   mvn clean install
   ```

---

## Running the Application

**Command Line:**
```bash
mvn spring-boot:run
```

**IntelliJ:**
1. Open `LibraryManagementApplication.java`
2. Click the green arrow next to the `main` method

The API will start at:  
`http://localhost:8081`

---

## API Documentation

**Access Swagger UI:**  
`http://localhost:8081/swagger-ui.html`

---

## Sample Requests

### 1. Create a Book
```bash
curl -X POST -H "Content-Type: application/json" \
-d '{
  "title": "Cien años de soledad",
  "author": "Gabriel García Márquez",
  "isbn": "9780061120084",
  "publishedDate": "1967-05-30"
}' \
http://localhost:8081/api/books
```

### 2. Get All Books
```bash
curl http://localhost:8081/api/books
```

### 3. Get Book by ID
```bash
curl http://localhost:8081/api/books/1
```

### 4. Update a Book
```bash
curl -X PUT -H "Content-Type: application/json" \
-d '{
  "title": "One Hundred Years of Solitude",
  "author": "Gabriel García Márquez",
  "isbn": "9780061120084",
  "publishedDate": "1967-05-30"
}' \
http://localhost:8081/api/books/1
```

### 5. Delete a Book
```bash
curl -X DELETE http://localhost:8081/api/books/1
```

### 6. Search Books
```bash
curl "http://localhost:8081/api/books/search?query=Gabriel"
```

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/library/LibraryManagement/
│   │       ├── config/       # Swagger configuration
│   │       ├── controller/   # API endpoints
│   │       ├── exception/    # Error handling
│   │       ├── model/        # Entity classes
│   │       ├── repository/   # Database interfaces
│   │       └── Application.java
│   └── resources/            # Properties files
```

---

## License
MIT License - Feel free to modify and use for educational/commercial purposes.
