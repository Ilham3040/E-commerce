package com.example.shoppingapi.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoreDetailCreateDTO {
    @NotNull(message = "Store ID is required")
    private Long storeId;

    private String address;

    private String description;

}
