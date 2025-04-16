package com.example.shoppingapi.dto.response;

public class ShipmentVendorDTO {
    private Long vendorId;

    public ShipmentVendorDTO(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
}
