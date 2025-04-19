package com.example.shoppingapi.modelhelper;

import com.example.shoppingapi.model.Order;
import com.example.shoppingapi.model.Shipment;
import com.example.shoppingapi.model.ShipmentId;
import com.example.shoppingapi.model.ShipmentVendor;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ShipmentModelHelper implements ModelHelper<Shipment> {

    private final ModelHelper<ShipmentVendor> shipmentVendorModelHelper = ModelHelperFactory.getModelHelper(ShipmentVendor.class);
    private final ModelHelper<Order> orderHelper = ModelHelperFactory.getModelHelper(Order.class);

    @Override
    public Shipment createModel(Integer num) {
        ShipmentVendor vendor;
        Order order;
        ShipmentId id;

        if (num == 1) {
            vendor    = shipmentVendorModelHelper.createModel(1);
            order = orderHelper.createModel(1);
        } else {
            vendor    = shipmentVendorModelHelper.createModel(2);
            order = orderHelper.createModel(2);
        }

        id = ShipmentId.builder().vendorId(vendor.getVendorId()).orderId(order.getOrderId()).build();

        return Shipment.builder()
                        .id(id)
                        .shipmentVendor(vendor)
                        .order(order)
                        .build();
    }
}
