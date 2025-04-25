package com.example.shoppingapi.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDetailCreateDTO {
    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Product description is required")
    private String description;
}
