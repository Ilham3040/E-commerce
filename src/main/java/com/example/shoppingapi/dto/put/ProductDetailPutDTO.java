package com.example.shoppingapi.dto.put;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDetailPutDTO {

    @NotNull(message = "Description is required")
    private String description;
}
