package com.example.shoppingapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "shipment")
@Data
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private Long shipmentId;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private ShipmentVendor shipmentVendor;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

}
