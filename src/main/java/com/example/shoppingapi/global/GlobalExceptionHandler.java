package com.example.shoppingapi.global;

import com.example.shoppingapi.dto.response.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResponse<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return new ApiResponse<>("The email address is already in use", null, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponse<?> handleResourceNotFound(ResourceNotFoundException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Resource not found";
        return new ApiResponse<>(message, null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleGenericException(Exception ex) {
        return new ApiResponse<>("An unexpected error occurred", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

