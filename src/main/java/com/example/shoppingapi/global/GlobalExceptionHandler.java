package com.example.shoppingapi.global;

import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.global.exception.ForeignKeyConstraintException;
import com.example.shoppingapi.global.exception.OwnerRoleDeletionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        logger.error("Data Integrity Violation: {}", ex.getMessage(), ex);
        ApiResponse<String> response = new ApiResponse<>(
                "The email address is already in use",
                null,
                HttpStatus.CONFLICT
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFound(ResourceNotFoundException ex) {
        logger.error("Resource Not Found: {}", ex.getMessage(), ex);
        ApiResponse<String> response = new ApiResponse<>(
                ex.getMessage() != null ? ex.getMessage() : "Resource not found",
                null,
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        logger.warn("Validation failed: {}", errorMessages);
        ApiResponse<List<String>> response = new ApiResponse<>(
                "Validation failed",
                errorMessages,
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
        logger.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        ApiResponse<String> response = new ApiResponse<>(
                "An unexpected error occurred",
                null,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(ForeignKeyConstraintException.class)
    public ResponseEntity<String> handleForeignKeyConstraint(ForeignKeyConstraintException ex) {
        logger.error("Foreign Key Constraint Violation: {}", ex.getMessage(), ex);
        return new ResponseEntity<>("Custom message: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OwnerRoleDeletionException.class)
    public ResponseEntity<ApiResponse<?>> handleOwnerRoleDeletion(OwnerRoleDeletionException ex) {
        logger.error("Owner role deletion attempt: {}", ex.getMessage(), ex);
        ApiResponse<String> response = new ApiResponse<>(
                ex.getMessage(),
                null,
                HttpStatus.FORBIDDEN
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

}
