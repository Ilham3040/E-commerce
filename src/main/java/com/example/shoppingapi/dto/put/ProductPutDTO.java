package com.example.shoppingapi.dto.put;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ProductPutDTO {

    @NotNull(message = "Product name is required")
    @Size(max = 40, message = "Product name must not exceed 40 characters")
    private String productName;
}
