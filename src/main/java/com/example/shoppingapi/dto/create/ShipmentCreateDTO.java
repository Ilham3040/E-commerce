package com.example.shoppingapi.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShipmentCreateDTO {
    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Shipment Vendor ID is required")
    private Long vendorId;
}
