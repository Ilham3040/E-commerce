package com.example.shoppingapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private Long userId;
    private Long productId;
    private String status;
}
