package com.example.shoppingapi.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShipmentDTO {
    private Long shipmentId;
    private Long vendorId;
    private Long orderId;
}
