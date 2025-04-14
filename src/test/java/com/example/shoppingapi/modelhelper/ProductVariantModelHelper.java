package com.example.shoppingapi.modelhelper;

import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.ProductVariant;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class ProductVariantModelHelper implements ModelHelper<ProductVariant> {

    private ModelHelper<Product> productHelper =  ModelHelperFactory.getModelHelper(Product.class);

    @Override
    public ProductVariant createModel(Integer num) {
        if (num == 1) {
            return ProductVariant.builder()
                .variantId(1L)
                .variantName("Standard Variant")
                .productReviews(100)
                .stockQuantity(50)
                .totalSold(20)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .product(productHelper.createModel(1))
                .build();
        } else {
            return ProductVariant.builder()
                .variantId(2L)
                .variantName("Deluxe Variant")
                .productReviews(200)
                .stockQuantity(30)
                .totalSold(40)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .product(productHelper.createModel(2))
                .build();
        }
    }
}
