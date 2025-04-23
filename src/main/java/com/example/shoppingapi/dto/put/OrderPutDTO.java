package com.example.shoppingapi.dto.put;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OrderPutDTO {

    @NotNull(message = "Status is required")
    @Pattern(regexp = "^(pending|completed|canceled)$", message = "Status must be 'pending', 'completed', or 'canceled'")
    private String status;
}
