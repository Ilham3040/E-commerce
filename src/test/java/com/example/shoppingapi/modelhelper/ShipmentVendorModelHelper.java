package com.example.shoppingapi.modelhelper;

import com.example.shoppingapi.model.ShipmentVendor;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ShipmentVendorModelHelper implements ModelHelper<ShipmentVendor> {

    @Override
    public ShipmentVendor createModel(Integer num){
        if (num == 1) {
            return ShipmentVendor.builder()
                .vendorId(1L)
                .vendorName("JohnDoe")
                .vendorEmail("johndoe@mymail.com")
                .vendorContact("0888888888888")
                .build();
        } else {
            return ShipmentVendor.builder()
                .vendorId(2L)
                .vendorName("Jonathan")
                .vendorEmail("jonathan@mymail.com")
                .vendorContact("0888888")
                .build();
        }
    }
}
