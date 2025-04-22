package com.example.shoppingapi.dto.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StoreRoleUpdateDTO {

    @Pattern(regexp = "^(admin|null)$", message = "Status must be 'admin', or 'null'")
    private String role;
}