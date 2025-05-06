package com.example.shoppingapi.modelhelper;
import com.example.shoppingapi.model.*;

import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Supplier;

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
        registry.put(OrderItem.class, OrderItemModelHelper::new);
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


