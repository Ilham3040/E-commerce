package com.example.shoppingapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDetailDTO {
    private Long productDetailId;
    private Long productId;
}
