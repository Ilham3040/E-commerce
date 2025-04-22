package com.example.shoppingapi.dto.update;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderUpdateDTO {

    @NotNull(message = "Status is required")
    @Pattern(regexp = "^(pending|completed|canceled)$", message = "Status must be 'pending', 'completed', or 'canceled'")
    private String status;

}