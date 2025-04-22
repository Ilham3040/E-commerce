package com.example.shoppingapi.dto.response;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShipmentDTO {
    private Long vendorId;
    private Long orderId;
}
