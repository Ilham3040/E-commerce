package com.example.shoppingapi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoreDetailRequestDTO {
    @NotNull(message = "Store ID is required")
    private Long storeId;

    private String address;

    private String description;

}
