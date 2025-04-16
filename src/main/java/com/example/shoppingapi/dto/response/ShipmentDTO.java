package com.example.shoppingapi.dto.response;

public class ShipmentDTO {
    private Long shipmentId;
    private Long vendorId;
    private Long orderId;

    public ShipmentDTO(Long shipmentId, Long vendorId, Long orderId) {
        this.shipmentId = shipmentId;
        this.vendorId = vendorId;
        this.orderId = orderId;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
