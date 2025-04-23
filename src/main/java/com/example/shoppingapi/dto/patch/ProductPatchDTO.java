package com.example.shoppingapi.dto.patch;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductPatchDTO {
    @Size(max = 40, message = "Product name must not exceed 40 characters")
    private String productName;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private BigDecimal price;

}
