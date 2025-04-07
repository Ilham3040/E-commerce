package com.example.shoppingapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "shipment")
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

    // Getters and Setters

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public ShipmentVendor getShipmentVendor() {
        return shipmentVendor;
    }

    public void setShipmentVendor(ShipmentVendor shipmentVendor) {
        this.shipmentVendor = shipmentVendor;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
