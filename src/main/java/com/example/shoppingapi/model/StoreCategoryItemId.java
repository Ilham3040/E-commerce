package com.example.shoppingapi.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class StoreCategoryItemId implements Serializable {

    private Long categoryId;
    private Long productId;

    // Default constructor
    public StoreCategoryItemId() {}

    // Parameterized constructor
    public StoreCategoryItemId(Long categoryId, Long productId) {
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
