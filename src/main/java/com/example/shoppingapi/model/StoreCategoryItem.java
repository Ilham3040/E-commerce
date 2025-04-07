package com.example.shoppingapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "store_categories_item")
public class StoreCategoryItem {

    @EmbeddedId
    private StoreCategoryItemId id;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", nullable = false)
    private StoreCategory storeCategory;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Getters and Setters

    public StoreCategoryItemId getId() {
        return id;
    }

    public void setId(StoreCategoryItemId id) {
        this.id = id;
    }

    public StoreCategory getStoreCategory() {
        return storeCategory;
    }

    public void setStoreCategory(StoreCategory storeCategory) {
        this.storeCategory = storeCategory;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
