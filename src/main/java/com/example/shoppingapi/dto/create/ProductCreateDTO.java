package com.example.shoppingapi.dto.create;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductCreateDTO {
    @NotNull(message = "Product name is required")
    @Size(max = 40, message = "Product name must not exceed 40 characters")
    private String productName;


    @NotNull(message = "Store ID is required")
    private Long storeId;
}
