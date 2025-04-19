package com.example.shoppingapi.modelhelper;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.model.UserCart;
import com.example.shoppingapi.model.UserFavorite;
import com.example.shoppingapi.model.Order;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.ProductDetail;
import com.example.shoppingapi.model.ProductReview;
import com.example.shoppingapi.model.ProductVariant;
import com.example.shoppingapi.model.ShipmentVendor;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.StoreCategory;
import com.example.shoppingapi.model.StoreDetail;
import com.example.shoppingapi.model.StoreRole;

import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Supplier;

import com.example.shoppingapi.model.Shipment;
import com.example.shoppingapi.model.StoreCategoryItem;

@NoArgsConstructor
public class ModelHelperFactory {

    private static final Map<Class<?>, Supplier<? extends ModelHelper<?>>> registry = new HashMap<>();

    static {
        registry.put(User.class, UserModelHelper::new);
        registry.put(UserCart.class, UserCartModelHelper::new);
        registry.put(UserFavorite.class, UserFavoriteModelHelper::new);
        registry.put(Store.class, StoreModelHelper::new);
        registry.put(StoreDetail.class, StoreDetailModelHelper::new);
        registry.put(StoreCategory.class, StoreCategoryModelHelper::new);
        registry.put(StoreCategoryItem.class, StoreCategoryItemModelHelper::new);
        registry.put(StoreRole.class, StoreRoleModelHelper::new);
        registry.put(Product.class, ProductModelHelper::new);
        registry.put(ProductDetail.class, ProductDetailModelHelper::new);
        registry.put(ProductVariant.class, ProductVariantModelHelper::new);
        registry.put(ProductReview.class, ProductReviewModelHelper::new);
        registry.put(Order.class, OrderModelHelper::new);
        registry.put(Shipment.class, ShipmentModelHelper::new);
        registry.put(ShipmentVendor.class, ShipmentVendorModelHelper::new);
    }

    @SuppressWarnings("unchecked")
    public static <T> ModelHelper<T> getModelHelper(Class<T> clazz) {
        Supplier<? extends ModelHelper<?>> supplier = registry.get(clazz);
        if (supplier == null) {
            throw new IllegalArgumentException("Unsupported model class: " + clazz.getName());
        }
        return (ModelHelper<T>) supplier.get();
    }
}


