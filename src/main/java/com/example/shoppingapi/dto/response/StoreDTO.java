package com.example.shoppingapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreDTO {
    private Long storeId;
    private Long userId;
}
