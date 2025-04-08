package com.example.shoppingapi.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
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

    // Override equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreCategoryItemId)) return false;
        StoreCategoryItemId that = (StoreCategoryItemId) o;
        return Objects.equals(getCategoryId(), that.getCategoryId()) &&
               Objects.equals(getProductId(), that.getProductId());
    }

    // Override hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(getCategoryId(), getProductId());
    }
}
