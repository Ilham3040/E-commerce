package com.example.shoppingapi.dto.put;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StoreCategoryPutDTO {

    @NotNull(message = "Category name is required")
    @Size(max = 25, message = "Category name must not exceed 25 characters")
    private String categoryName;
}
