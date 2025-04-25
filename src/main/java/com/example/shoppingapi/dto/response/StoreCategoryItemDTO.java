package com.example.shoppingapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreCategoryItemDTO {
    private Long categoryId;
    private Long productId;
}
