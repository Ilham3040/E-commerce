package com.example.shoppingapi.dto.patch;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVariantPatchDTO {
    @Size(max = 25, message = "Variant name must not exceed 25 characters")
    private String variantName;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private BigDecimal price;

    private Integer stockQuantity;
}
