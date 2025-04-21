package com.example.shoppingapi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoreCategoryItemRequestDTO {
    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Product ID is required")
    private Long productId;
}
