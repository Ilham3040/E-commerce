package com.example.shoppingapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StoreRoleRequestDTO {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Store ID is required")
    private Long storeId;

    @NotNull(message = "User role in the store is required")
    @Pattern(regexp = "^(admin|owner|null)$", message = "Status must be 'admin', 'owner', or 'null'")
    private String role;
}
