package com.example.shoppingapi.modelhelper;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.model.Order;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.ProductDetail;
import com.example.shoppingapi.model.ProductReview;
import com.example.shoppingapi.model.ProductVariant;
import com.example.shoppingapi.model.Store;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Supplier;



public class ModelHelperFactory {

    private static final Map<Class<?>, Supplier<? extends ModelHelper<?>>> registry = new HashMap<>();

    private ModelHelperFactory() {
    }

    static {
        registry.put(User.class, UserModelHelper::new);
        registry.put(Store.class, StoreModelHelper::new);
        registry.put(Product.class, ProductModelHelper::new);
        registry.put(ProductDetail.class, ProductDetailModelHelper::new);
        registry.put(ProductVariant.class, ProductVariantModelHelper::new);
        // registry.put(ProductReview.class, ProductReviewModelHelper::new);
        registry.put(Order.class, OrderModelHelper::new);
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


