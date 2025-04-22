package com.example.shoppingapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipment")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Min(value = 1, message = "Status code range is 1-16 check documentation for further explaination")
    @Max(value = 16, message = "Status code range is 1-16{value} check documentation for further explaination")
    @Digits(integer = 1, fraction = 0, message = "Status code must be an integer. Status code range is 1-16 check documentation for further explaination")
    @Builder.Default
    @Column(name = "shipment_status")
    private Integer shipmentStatus = 1;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }


}
