package com.example.shoppingapi.modelhelper;

import com.example.shoppingapi.model.Store;

public class StoreModelHelper implements ModelHelper<Store> {

    public StoreModelHelper() {
    }

    @Override
    public Store createModel(Integer num) {
        Store store = new Store();
        if (num == 1) {
            store.setStoreId(1L);
            store.setStoreName("Pet shop");
        } else {
            store.setStoreId(2L);
            store.setStoreName("Book store");
        }
        return store;
    }
}
