package com.example.shoppingapi.modelhelper;
import com.example.shoppingapi.model.User;
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


