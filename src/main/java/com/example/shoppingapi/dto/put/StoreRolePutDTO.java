package com.example.shoppingapi.dto.put;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StoreRolePutDTO {

    @NotNull(message = "Role is required")
    @Pattern(regexp = "^(admin|null)$", message = "Role must be 'admin' or 'null'")
    private String role;
}
