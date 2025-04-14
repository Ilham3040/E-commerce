package com.example.shoppingapi.dto.response;

public class StoreCategoryItemDTO {
    private Long categoryId;
    private Long productId;

    public StoreCategoryItemDTO(Long categoryId, Long productId) {
        this.categoryId = categoryId;
        this.productId = productId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
