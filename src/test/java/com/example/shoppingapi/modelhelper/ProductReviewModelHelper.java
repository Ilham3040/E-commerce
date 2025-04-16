package com.example.shoppingapi.modelhelper;

import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.ProductReview;
import com.example.shoppingapi.model.User;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductReviewModelHelper implements ModelHelper<ProductReview> {

    private ModelHelper<Product> productHelper =  ModelHelperFactory.getModelHelper(Product.class);
    private ModelHelper<User> userHelper =  ModelHelperFactory.getModelHelper(User.class);

    @Override
    public ProductReview createModel(Integer num) {
        if (num == 1) {
            return ProductReview.builder()
                .reviewId(1L)
                .description("Here is the description")
                .starRating(5)
                .product(productHelper.createModel(1))
                .user(userHelper.createModel(1))
                .build();
        } else {
            return ProductReview.builder()
                .reviewId(2L)
                .description("Here is the description")
                .starRating(3)
                .product(productHelper.createModel(2))
                .user(userHelper.createModel(2))
                .build();
        }
    }
}
