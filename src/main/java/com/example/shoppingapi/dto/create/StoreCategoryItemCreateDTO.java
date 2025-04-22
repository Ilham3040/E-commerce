package com.example.shoppingapi.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoreCategoryItemCreateDTO {
    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Product ID is required")
    private Long productId;
}
