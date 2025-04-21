package com.example.shoppingapi.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StoreCategoryRequestDTO {
    @NotNull(message = "Store ID is required")
    private Long storeId;

    @NotNull(message = "Category Name is required")
    private String categoryName;
}
