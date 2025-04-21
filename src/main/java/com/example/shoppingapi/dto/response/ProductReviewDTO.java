package com.example.shoppingapi.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductReviewDTO {
    private Long productReviewId;
    private Long userId;
    private Long productId;

}


