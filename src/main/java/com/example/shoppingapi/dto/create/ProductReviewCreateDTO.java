package com.example.shoppingapi.dto.create;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductReviewCreateDTO {
    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Star Rating is required")
    @Min(value = 1, message = "Star rating must be at least {value}")
    @Max(value = 5, message = "Star rating must be at most {value}")
    @Digits(integer = 1, fraction = 0, message = "Star rating must be an integer")
    private Integer starRating;

    private String description;
}
