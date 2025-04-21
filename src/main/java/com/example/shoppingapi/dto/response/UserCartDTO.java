package com.example.shoppingapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCartDTO {
    private Long cartId;
    private Long userId;
    private Long productId;

}
