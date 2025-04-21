package com.example.shoppingapi.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class ApiResponse<T> {

    private HttpStatus status; // HTTP status from HttpStatus enum
    private String message;     // Message explaining the result
    private T data;             // Data to be returned in case of success

    // Constructor for success (with data)
    public ApiResponse(String message, T data,HttpStatus status) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
