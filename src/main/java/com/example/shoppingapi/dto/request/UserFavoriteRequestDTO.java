package com.example.shoppingapi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserFavoriteRequestDTO {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Product ID is required")
    private Long productId;
}
