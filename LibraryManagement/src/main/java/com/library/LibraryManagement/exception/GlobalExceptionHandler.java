package com.library.LibraryManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<com.library.LibraryManagement.exception.ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        com.library.LibraryManagement.exception.ErrorResponse response = new com.library.LibraryManagement.exception.ErrorResponse(ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<com.library.LibraryManagement.exception.ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .orElse("Validation error");
        com.library.LibraryManagement.exception.ErrorResponse response = new com.library.LibraryManagement.exception.ErrorResponse(errorMsg, LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}