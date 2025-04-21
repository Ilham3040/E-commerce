package com.example.shoppingapi.model;

import java.time.LocalDateTime;



import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Table(name = "store_role")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
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

    @Column(name = "deleted_at",columnDefinition = "TIMESTAMPTZ")
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
}
