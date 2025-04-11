package com.example.shoppingapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "store_categories_item")
@Data
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

}
