package com.example.shoppingapi.dto.create;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
public class OrderItemCreateDTO {
    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Product Variant ID is required")
    private Long productVariantId;

    @NotNull(message = "Unit Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private BigDecimal unitPrice;

    @NotNull(message = "Quantity is required")
    @Digits(integer = 1, fraction = 0, message = "Quantity must be an integer")
    private int quantity;
}
