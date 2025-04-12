package com.example.shoppingapi.dto;

public class StoreDTO {
    private Long storeId;
    private Long userId;

    public StoreDTO(Long storeId,Long userId) {
        this.storeId = storeId;
        this.userId = userId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
