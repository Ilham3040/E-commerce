package com.example.shoppingapi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoreDetailRequestDTO {
    @NotNull(message = "Store ID is required")
    private Long storeId;

    @NotNull(message = "Store Address is required")
    private String address;

    @NotNull(message = "Store Description is required")
    private String description;

}
