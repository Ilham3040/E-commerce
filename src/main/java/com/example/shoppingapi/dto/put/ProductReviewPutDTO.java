package com.example.shoppingapi.dto.put;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductReviewPutDTO {

    @NotNull(message = "Star rating is required")
    @Min(value = 1, message = "Star rating must be at least {value}")
    @Max(value = 5, message = "Star rating must be at most {value}")
    @Digits(integer = 1, fraction = 0, message = "Star rating must be an integer")
    private Integer starRating;

    @NotNull(message = "Description is required")
    private String description;
}
