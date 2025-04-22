package com.example.shoppingapi.dto.create;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVariantCreateDTO {
    @NotNull(message = "Variant name is required")
    @Size(max = 25, message = "Variant name must not exceed 25 characters")
    private String variantName;

    @NotNull(message = "Store ID is required")
    private Long storeId;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private BigDecimal price;
}
