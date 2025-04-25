package com.example.shoppingapi.dto.patch;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductReviewPatchDTO {
    @Min(value = 1, message = "Star rating must be at least {value}")
    @Max(value = 5, message = "Star rating must be at most {value}")
    @Digits(integer = 1, fraction = 0, message = "Star rating must be an integer")
    private Integer starRating;

    private String description;
}
