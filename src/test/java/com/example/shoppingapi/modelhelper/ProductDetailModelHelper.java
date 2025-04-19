package com.example.shoppingapi.modelhelper;

import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.ProductDetail;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;

@NoArgsConstructor
public class ProductDetailModelHelper implements ModelHelper<ProductDetail> {

    private final ModelHelper<Product> productHelper =  ModelHelperFactory.getModelHelper(Product.class);

    @Override
    public ProductDetail createModel(Integer num) {
        if (num == 1) {
            return ProductDetail.builder()
                .productDetailId(1L)
                .product(productHelper.createModel(1))
                .attachmentUrls(Arrays.asList(
                    "http://example.com/image1.jpg",
                    "http://example.com/image2.jpg"))
                .totalSold(100)
                .reviewRating(BigDecimal.valueOf(4.50))
                .build();
        } else {
            return ProductDetail.builder()
                .productDetailId(2L)
                .product(productHelper.createModel(2))
                .attachmentUrls(Arrays.asList(
                    "http://example.com/image3.jpg",
                    "http://example.com/image4.jpg"))
                .totalSold(200)
                .reviewRating(BigDecimal.valueOf(3.75))
                .build();
        }
    }
}
