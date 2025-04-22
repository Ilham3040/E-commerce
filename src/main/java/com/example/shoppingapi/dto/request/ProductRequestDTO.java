package com.example.shoppingapi.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDTO {
    @NotNull(message = "Product name is required")
    @Size(max = 40, message = "Product name must not exceed 40 characters")
    private String productName;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Store ID is required")
    private Long storeId;
}
