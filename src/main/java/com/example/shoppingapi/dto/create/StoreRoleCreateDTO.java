package com.example.shoppingapi.dto.create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StoreRoleCreateDTO {
    @NotNull(message = "Store ID is required")
    private Long storeId;

    @NotNull(message = "User ID is required")
    private Long userId;
}
