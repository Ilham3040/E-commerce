package com.example.shoppingapi.dto.update;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StoreCategoryUpdateDTO {

    @Size(max = 25, message = "Category name must not exceed 25 characters")
    private String categoryName;
}