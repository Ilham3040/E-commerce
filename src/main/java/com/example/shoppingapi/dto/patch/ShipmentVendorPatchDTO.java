package com.example.shoppingapi.dto.patch;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ShipmentVendorPatchDTO {

    @Size(max = 30, message = "Vendor name must not exceed 30 characters")
    private String vendorName;

    @Size(max = 15, message = "Vendor contact must not exceed 15 characters")
    private String vendorContact;

    @Size(max = 15, message = "Vendor email must not exceed 15 characters")
    private String vendorEmail;

    private String officialWebsiteUrl;
}
