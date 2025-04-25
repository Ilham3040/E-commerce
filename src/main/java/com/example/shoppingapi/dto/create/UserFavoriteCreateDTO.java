package com.example.shoppingapi.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserFavoriteCreateDTO {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Product ID is required")
    private Long productId;
}
