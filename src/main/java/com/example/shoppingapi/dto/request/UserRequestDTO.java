package com.example.shoppingapi.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequestDTO {
    @NotBlank(message = "Username is required.")
    @Size(max = 30, message = "Username must not exceed 30 characters")
    private String username;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be valid.")
    private String email;

    @NotBlank(message = "Phone Number is required.")
    @Size(max = 16, message = "Phone Number must not exceed 16 characters")
    private String phoneNumber;
}
