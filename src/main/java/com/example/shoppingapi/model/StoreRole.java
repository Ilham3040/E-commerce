package com.example.shoppingapi.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.type.YesNoConverter;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;


@Entity
@Data
@Table(name = "store_role")
@Builder(toBuilder = true)
@SoftDelete(columnName = "deleted_at", strategy = SoftDeleteType.DELETED, converter = YesNoConverter.class)
public class StoreRole {

    @EmbeddedId
    private StoreRoleId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName="id" ,nullable = false)
    private User user;

    @ManyToOne
    @MapsId("storeId")
    @JoinColumn(name = "store_id", referencedColumnName="id",nullable = false)
    private Store store;

    @Builder.Default
    @Column(name = "role", length = 50, columnDefinition = "VARCHAR(50) DEFAULT 'null'")
    private String role = "null";

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime deletedAt;

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
        this.deletedAt = LocalDateTime.now();
    }
}
