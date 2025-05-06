package com.example.shoppingapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderItemDTO {
    private Long orderItemId;
    private Long orderId;
    private Long productId;
    private Long productVariantId;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal lineTotal;
}
