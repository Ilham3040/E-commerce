package com.example.shoppingapi.dto;

public class StoreDTO {
    private Long storeId;
    private String storeName;

    public StoreDTO(Long storeId, String storeName) {
        this.storeId = storeId;
        this.storeName = storeName;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
