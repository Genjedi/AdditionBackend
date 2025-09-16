package com.casaleff.addition.error;

import org.springframework.http.HttpStatus;

public enum ErrorType {
    
    // Common errors
    BAD_REQUEST("E001", "Invalid request", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("E002", "Unauthorized access", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("E003", "Forbidden request", HttpStatus.FORBIDDEN),
    NOT_FOUND("E004", "Resource not found", HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED("E005", "Method not allowed", HttpStatus.METHOD_NOT_ALLOWED),
    INTERNAL_SERVER_ERROR("E006", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),

    // Database-related errors
    DATA_INTEGRITY_VIOLATION("D001", "Data integrity violation", HttpStatus.CONFLICT),
    ENTITY_NOT_FOUND("D002", "Entity not found in database", HttpStatus.NOT_FOUND),
    DUPLICATE_ENTRY("D003", "Duplicate entry", HttpStatus.CONFLICT),

    // Validation errors
    INVALID_INPUT("V001", "Invalid input provided", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_FIELD("V002", "Missing required field", HttpStatus.BAD_REQUEST),

    // Authentication & Authorization errors
    INVALID_CREDENTIALS("A001", "Invalid credentials", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("A002", "Access denied", HttpStatus.FORBIDDEN),

    // File Upload Errors
    INVALID_FILE_TYPE("F001", "Invalid file type", HttpStatus.BAD_REQUEST),
    FILE_SIZE_EXCEEDED("F002", "File size exceeds the allowed limit", HttpStatus.PAYLOAD_TOO_LARGE);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorType(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
