package com.example.shoppingapi.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "store_role")
public class StoreRole {

    @EmbeddedId
    private StoreRoleId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("storeId")
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "role", length = 50, columnDefinition = "VARCHAR(50) DEFAULT 'null'")
    private String role = "null";
}
