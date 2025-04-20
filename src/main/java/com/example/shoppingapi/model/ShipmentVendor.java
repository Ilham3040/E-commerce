package com.example.shoppingapi.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.type.YesNoConverter;

@Entity
@Table(name = "shipment_vendor")
@Data
@Builder(toBuilder = true)
@SoftDelete(columnName = "is_deleted", strategy = SoftDeleteType.DELETED, converter = YesNoConverter.class)
public class ShipmentVendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long vendorId;

    @Column(name = "vendor_name", length = 255, nullable = false)
    private String vendorName;

    @Column(name = "vendor_contact", length = 255)
    private String vendorContact;

    @Column(name = "vendor_email", length = 255, unique = true)
    private String vendorEmail;

    @Column(name = "official_website_url", columnDefinition = "TEXT")
    private String officialWebsiteUrl;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at",columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted", nullable = false,insertable = false, updatable = false)
    @Convert(converter = YesNoConverter.class)
    @Builder.Default
    private boolean isDeleted = false;

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

    @PreRemove
    public void preRemove() {
        deletedAt = LocalDateTime.now();
    }
}
