package com.example.shoppingapi.modelhelper;

import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.StoreCategory;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StoreCategoryModelHelper implements ModelHelper<StoreCategory> {

    private final ModelHelper<Store> storeHelper = ModelHelperFactory.getModelHelper(Store.class);

    @Override
    public StoreCategory createModel(Integer num) {
        if (num == 1) {
            return StoreCategory.builder()
                                .categoryId(1L)
                                .categoryName("Cat Food")
                                .store(storeHelper.createModel(1))
                                .build();
        }
        else {
            return StoreCategory.builder()
                                .categoryId(1L)
                                .categoryName("Cat Food")
                                .store(storeHelper.createModel(2))
                                .build();
        }

    }

}