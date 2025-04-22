package com.example.shoppingapi.dto.create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StoreRoleCreateDTO {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Store ID is required")
    private Long storeId;

    @NotNull(message = "User role in the store is required")
    @Pattern(regexp = "^(admin|null)$", message = "Status must be 'admin' or 'null'")
    private String role;
}
