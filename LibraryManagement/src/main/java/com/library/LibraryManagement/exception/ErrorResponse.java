package com.library.LibraryManagement.exception;

import java.time.LocalDateTime;

public record ErrorResponse(String message, LocalDateTime timestamp) {
}