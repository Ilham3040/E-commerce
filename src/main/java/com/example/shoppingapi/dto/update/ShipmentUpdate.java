package com.example.shoppingapi.dto.update;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ShipmentUpdate {

    @Min(value = 1, message = "Status code range is 1-16 check documentation for further explaination")
    @Max(value = 16, message = "Status code range is 1-16{value} check documentation for further explaination")
    @Digits(integer = 1, fraction = 0, message = "Status code must be an integer. Status code range is 1-16 check documentation for further explaination")
    private Integer shipmentStatus;
}
