package com.example.shoppingapi.dto.patch;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StoreCategoryPatchDTO {

    @Size(max = 25, message = "Category name must not exceed 25 characters")
    private String categoryName;
}