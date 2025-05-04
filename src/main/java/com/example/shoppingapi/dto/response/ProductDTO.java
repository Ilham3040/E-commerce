package com.example.shoppingapi.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    private Long storeId;
}
