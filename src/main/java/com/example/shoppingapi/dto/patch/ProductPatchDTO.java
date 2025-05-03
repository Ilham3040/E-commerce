package com.example.shoppingapi.dto.patch;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductPatchDTO {
    @Size(max = 40, message = "Product name must not exceed 40 characters")
    private String productName;
}
