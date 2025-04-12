package com.example.shoppingapi.dto;

public class ProductDTO {
    private Long productId;
    private Long storeId;

    public ProductDTO(Long productId, Long storeId) {
        this.productId = productId;
        this.storeId = storeId;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
