package com.example.shoppingapi.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreCategoryDTO {
    private Long categoryId;
    private Long storeId;
}
