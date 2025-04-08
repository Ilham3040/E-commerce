package com.example.shoppingapi.model;

import jakarta.persistence.*;

@Entity
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

    // Getter and Setter methods

    public StoreRoleId getId() {
        return id;
    }

    public void setId(StoreRoleId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
