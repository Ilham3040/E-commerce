package com.example.shoppingapi.dto.create;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StoreCategoryCreateDTO {
    @NotNull(message = "Store ID is required")
    private Long storeId;

    @NotNull(message = "Category Name is required")
    @Size(max = 25, message = "Category name must not exceed 25 characters")
    private String categoryName;
}