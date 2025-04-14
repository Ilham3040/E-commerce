package com.example.shoppingapi.modelhelper;

import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.User;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StoreModelHelper implements ModelHelper<Store> {

    private ModelHelper<User> userHelper =  ModelHelperFactory.getModelHelper(User.class);

    @Override
    public Store createModel(Integer num) {
        if (num == 1) {
            return Store.builder()
                .storeId(1L)
                .storeName("Pet shop")
                .user(userHelper.createModel(1))
                .build();
        } else {
            return Store.builder()
                .storeId(2L)
                .storeName("Book store")
                .user(userHelper.createModel(2))
                .build();
        }
    }
}
