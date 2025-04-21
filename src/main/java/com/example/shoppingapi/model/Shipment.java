package com.example.shoppingapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shipment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Shipment {

    @EmbeddedId
    private ShipmentId id;

    @MapsId("vendorId")
    @ManyToOne
    @JoinColumn(name = "vendor_id",referencedColumnName="id", nullable = false)
    private ShipmentVendor shipmentVendor;

    @MapsId("orderId")
    @ManyToOne
    @JoinColumn(name = "order_id",referencedColumnName="id", nullable = false)
    private Order order;
}
