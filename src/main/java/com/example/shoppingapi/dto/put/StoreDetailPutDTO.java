package com.example.shoppingapi.dto.put;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoreDetailPutDTO {

    @NotNull(message = "Address is required")
    private String address;

    @NotNull(message = "Description is required")
    private String description;
}
