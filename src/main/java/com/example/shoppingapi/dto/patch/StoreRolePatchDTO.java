package com.example.shoppingapi.dto.patch;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StoreRolePatchDTO {

    @Pattern(regexp = "^(admin|null)$", message = "Status must be 'admin', or 'null'")
    private String role;
}