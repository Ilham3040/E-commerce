package com.example.shoppingapi.dto.create;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCreateDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Status is required")
    @Pattern(regexp = "^(pending|completed|canceled)$", message = "Status must be 'pending', 'completed', or 'canceled'")
    private String status;

    private BigDecimal totalPrice;
    
}