package com.example.shoppingapi.dto.patch;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserPatchDTO {
    @Size(max = 30, message = "Username must not exceed 30 characters")
    private String username;

    @Email(message = "Email should be valid.")
    private String email;

    @Size(max = 16, message = "Phone Number must not exceed 16 characters")
    private String phoneNumber;
}