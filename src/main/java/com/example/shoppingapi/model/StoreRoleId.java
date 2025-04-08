package com.example.shoppingapi.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StoreRoleId implements Serializable {

    private Long userId;
    private Long storeId;

    // Default constructor
    public StoreRoleId() {}

    // Parameterized constructor
    public StoreRoleId(Long userId, Long storeId) {
        this.userId = userId;
        this.storeId = storeId;
    }

    // Getter for userId
    public Long getUserId() {
        return userId;
    }

    // Setter for userId
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Getter for storeId
    public Long getStoreId() {
        return storeId;
    }

    // Setter for storeId
    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    // Override equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreRoleId)) return false;
        StoreRoleId that = (StoreRoleId) o;
        return Objects.equals(getUserId(), that.getUserId()) &&
                Objects.equals(getStoreId(), that.getStoreId());
    }

    // Override hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getStoreId());
    }
}
