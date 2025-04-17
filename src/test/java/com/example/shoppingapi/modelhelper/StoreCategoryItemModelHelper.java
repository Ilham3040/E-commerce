package com.example.shoppingapi.modelhelper;

import com.example.shoppingapi.model.StoreCategoryItemId;


import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.StoreCategory;
import com.example.shoppingapi.model.StoreCategoryItem;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StoreCategoryItemModelHelper implements ModelHelper<StoreCategoryItem> {

    private ModelHelper<StoreCategory> storeCategoryHelper = ModelHelperFactory.getModelHelper(StoreCategory.class);
    private ModelHelper<Product> productHelper = ModelHelperFactory.getModelHelper(Product.class);

    @Override
    public StoreCategoryItem createModel(Integer num) {
        StoreCategory category;
        Product product;
        StoreCategoryItemId id;

        if (num == 1) {
            category    = storeCategoryHelper.createModel(1);
            product = productHelper.createModel(1);
        } else {
            category    = storeCategoryHelper.createModel(2);
            product = productHelper.createModel(2);
        }

        id = new StoreCategoryItemId();
        id.setCategoryId(category.getCategoryId());
        id.setProductId(product.getProductId());

        return StoreCategoryItem.builder()
                        .id(id)
                        .storeCategory(category)
                        .product(product)
                        .build();
    }
}
