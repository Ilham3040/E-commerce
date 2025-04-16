package com.example.shoppingapi.model;

import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.type.YesNoConverter;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "shipment")
@Data
@Builder(toBuilder = true)
@SoftDelete(columnName = "deleted_at", strategy = SoftDeleteType.DELETED, converter = YesNoConverter.class)
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
