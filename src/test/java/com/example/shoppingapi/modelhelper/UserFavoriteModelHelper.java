package com.example.shoppingapi.modelhelper;

import com.example.shoppingapi.model.UserFavorite;
import com.example.shoppingapi.model.UserFavoriteId;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.model.Product;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserFavoriteModelHelper implements ModelHelper<UserFavorite> {

    private ModelHelper<User> userHelper = ModelHelperFactory.getModelHelper(User.class);
    private ModelHelper<Product> productHelper = ModelHelperFactory.getModelHelper(Product.class);

    @Override
    public UserFavorite createModel(Integer num) {
        User user;
        Product product;
        UserFavoriteId id;

        if (num == 1) {
            user    = userHelper.createModel(1);
            product = productHelper.createModel(1);
        } else {
            user    = userHelper.createModel(2);
            product = productHelper.createModel(2);
        }
        
        id = new UserFavoriteId(user.getUserId(), product.getProductId());
        return UserFavorite.builder()
                        .id(id)
                        .user(user)
                        .product(product)
                        .build();
    }
}
