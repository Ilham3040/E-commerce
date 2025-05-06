package com.example.shoppingapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private Long userId;
    private Long productId;
    private String status;
    private BigDecimal shippingPrice;
    private BigDecimal servicePrice;
    private BigDecimal totalPrice;
}
