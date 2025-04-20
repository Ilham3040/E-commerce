// src/main/java/com/example/shoppingapi/dto/request/StoreRequestDTO.java
package com.example.shoppingapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StoreRequestDTO {
    @NotNull(message = "Store name is required")
    @Size(max = 100, message = "Store name must not exceed 100 characters")
    private String storeName;

    @NotNull(message = "User ID is required")
    private Long userId;
}
