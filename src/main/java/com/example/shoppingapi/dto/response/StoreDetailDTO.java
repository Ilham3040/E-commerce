package com.example.shoppingapi.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreDetailDTO {
    private Long storeDetailId;
    private Long storeId;
}
