package com.example.shoppingapi.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StoreCategoryRequestDTO {
    @NotNull(message = "Store ID is required")
    private Long storeId;

    @NotNull(message = "Category Name is required")
    @Size(max = 25, message = "Category name must not exceed 25 characters")
    private String categoryName;
}
