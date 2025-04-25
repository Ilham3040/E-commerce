package com.example.shoppingapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreRoleDTO {
    private Long userId;
    private Long storeId;
    private String role;


}
