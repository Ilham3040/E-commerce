package com.example.shoppingapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductVariantRequestDTO {
    @NotNull(message = "Variant name is required")
    @Size(max = 25, message = "Variant name must not exceed 25 characters")
    private String variantName;

    @NotNull(message = "Store ID is required")
    private Long storeId;

    private Integer stockQuantity;
}
