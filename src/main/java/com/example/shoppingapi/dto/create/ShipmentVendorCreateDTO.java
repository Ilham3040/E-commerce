package com.example.shoppingapi.dto.create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ShipmentVendorCreateDTO {

    @NotNull(message = "Vendor Name is required")
    @Size(max = 30, message = "Vendor name must not exceed 30 characters")
    private String vendorName;

    @NotNull(message = "Vendor Contact is required")
    @Size(max = 15, message = "Vendor contact must not exceed 15 characters")
    private String vendorContact;

    @NotNull(message = "Vendor Email is required")
    @Size(max = 15, message = "Vendor email must not exceed 15 characters")
    private String vendorEmail;

    private String officialWebsiteUrl;
}
