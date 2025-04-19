package com.example.shoppingapi.modelhelper;

import com.example.shoppingapi.model.UserCart;
import com.example.shoppingapi.model.UserCartId;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.model.Product;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserCartModelHelper implements ModelHelper<UserCart> {

    private final ModelHelper<User> userHelper = ModelHelperFactory.getModelHelper(User.class);
    private final ModelHelper<Product> productHelper = ModelHelperFactory.getModelHelper(Product.class);

    @Override
    public UserCart createModel(Integer num) {
        User user;
        Product product;
        UserCartId id;
        int quantity;

        if (num == 1) {
            user    = userHelper.createModel(1);
            product = productHelper.createModel(1);
            quantity = 2;
        } else {
            user    = userHelper.createModel(2);
            product = productHelper.createModel(2);
            quantity = 5;
        }

        id = new UserCartId(user.getUserId(), product.getProductId());
        return UserCart.builder()
                        .id(id)
                        .user(user)
                        .product(product)
                        .quantity(quantity)
                        .build();
    }
}
