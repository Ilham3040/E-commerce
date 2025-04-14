package com.example.shoppingapi.modelhelper;

import com.example.shoppingapi.model.ProductVariant;
import com.example.shoppingapi.model.Product;
import java.time.LocalDateTime;
public class ProductVariantModelHelper implements ModelHelper<ProductVariant> {

    private final Long variantId1 = 1L;
    private final Long variantId2 = 2L;
    
    private final String variantName1 = "Standard Variant";
    private final String variantName2 = "Deluxe Variant";
    
    private final Integer productReviews1 = 100;
    private final Integer productReviews2 = 200;
    
    private final Integer stockQuantity1 = 50;
    private final Integer stockQuantity2 = 30;
    
    private final Integer totalSold1 = 20;
    private final Integer totalSold2 = 40;

    public ProductVariantModelHelper() {
    }

    @Override
    public ProductVariant createModel(Integer num) {
        if (num == 1) {
            ProductVariant pv1 = new ProductVariant();
            pv1.setVariantId(variantId1);
            pv1.setVariantName(variantName1);
            pv1.setProductReviews(productReviews1);
            pv1.setStockQuantity(stockQuantity1);
            pv1.setTotalSold(totalSold1);
            pv1.setCreatedAt(LocalDateTime.now());
            pv1.setUpdatedAt(LocalDateTime.now());

            // Create and set a dummy Product for this variant.
            Product product1 = new Product();
            product1.setProductId(1L);
            product1.setProductName("Cat Food");
            product1.setPrice(java.math.BigDecimal.valueOf(40000));
            pv1.setProduct(product1);

            return pv1;
        } else {
            ProductVariant pv2 = new ProductVariant();
            pv2.setVariantId(variantId2);
            pv2.setVariantName(variantName2);
            pv2.setProductReviews(productReviews2);
            pv2.setStockQuantity(stockQuantity2);
            pv2.setTotalSold(totalSold2);
            pv2.setCreatedAt(LocalDateTime.now());
            pv2.setUpdatedAt(LocalDateTime.now());

            // Create and set a dummy Product for this variant.
            Product product2 = new Product();
            product2.setProductId(2L);
            product2.setProductName("Fish Food");
            product2.setPrice(java.math.BigDecimal.valueOf(5000));
            pv2.setProduct(product2);

            return pv2;
        }
    }
}
