package com.example.shoppingapi.modelhelper;

import com.example.shoppingapi.model.StoreRole;
import com.example.shoppingapi.model.StoreRoleId;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.model.Store;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StoreRoleModelHelper implements ModelHelper<StoreRole> {

    private final ModelHelper<User> userHelper = ModelHelperFactory.getModelHelper(User.class);
    private final ModelHelper<Store> storeHelper = ModelHelperFactory.getModelHelper(Store.class);

    @Override
    public StoreRole createModel(Integer num) {
        User user;
        Store product;
        StoreRoleId id;
        String role;

        if (num == 1) {
            user    = userHelper.createModel(1);
            product = storeHelper.createModel(1);
            role = "owner";
        } else {
            user    = userHelper.createModel(2);
            product = storeHelper.createModel(2);
            role = "admin";
        }

        id = new StoreRoleId(user.getUserId(), product.getStoreId());
        return StoreRole.builder()
                        .id(id)
                        .user(user)
                        .store(product)
                        .role(role)
                        .build();
    }
}
