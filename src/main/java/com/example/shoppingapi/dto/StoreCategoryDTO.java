package com.example.shoppingapi.dto;

public class StoreCategoryDTO {
    private Long categoryId;
    private Long storeId;

    public StoreCategoryDTO(Long categoryId, Long storeId) {
        this.categoryId = categoryId;
        this.storeId = storeId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
