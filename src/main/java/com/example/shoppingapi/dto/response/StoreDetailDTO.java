package com.example.shoppingapi.dto.response;

public class StoreDetailDTO {
    private Long storeDetailId;
    private Long storeId;

    public StoreDetailDTO(Long storeDetailId, Long storeId) {
        this.storeDetailId = storeDetailId;
        this.storeId = storeId;
    }

    public Long getStoreDetailId() {
        return storeDetailId;
    }

    public void setStoreDetailId(Long storeDetailId) {
        this.storeDetailId = storeDetailId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
