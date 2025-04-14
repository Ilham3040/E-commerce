package com.example.shoppingapi.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "shipment")
@Data
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
