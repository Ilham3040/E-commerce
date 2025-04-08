package com.example.shoppingapi.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
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
}
