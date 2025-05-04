package com.example.shoppingapi.modelhelper;

import java.math.BigDecimal;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.Store;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductModelHelper implements ModelHelper<Product> {


    private final ModelHelper<Store> storeHelper =  ModelHelperFactory.getModelHelper(Store.class);

    @Override
    public Product createModel(Integer num) {
        if (num == 1) {
            return Product.builder()
                .productId(1L)
                .productName("Cat Food")
                .store(storeHelper.createModel(1))
                .build();
        } else {
            return Product.builder()
                .productId(2L)
                .productName("Fish food")
                .store(storeHelper.createModel(2))
                .build();
        }
    }
}
