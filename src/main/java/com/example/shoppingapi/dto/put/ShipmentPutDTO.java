package com.example.shoppingapi.dto.put;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShipmentPutDTO {

    @NotNull(message = "Shipment status code is required")
    @Min(value = 1, message = "Status code range is 1-16. Please check documentation for further explanation.")
    @Max(value = 16, message = "Status code range is 1-16. Please check documentation for further explanation.")
    @Digits(integer = 1, fraction = 0, message = "Status code must be an integer. Status code range is 1-16. Please check documentation for further explanation.")
    private Integer shipmentStatus;
}
