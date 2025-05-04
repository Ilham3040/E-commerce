package com.example.shoppingapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductVariantDTO {
    private Long productVariantId;
    private Long productId;
}
