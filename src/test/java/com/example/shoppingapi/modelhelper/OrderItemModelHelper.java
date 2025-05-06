package com.example.shoppingapi.modelhelper;

import com.example.shoppingapi.model.OrderItem;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.ProductVariant;
import com.example.shoppingapi.model.Order;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
public class OrderItemModelHelper implements ModelHelper<OrderItem> {

    private final ModelHelper<Order> orderHelper = ModelHelperFactory.getModelHelper(Order.class);
    private final ModelHelper<Product> productHelper = ModelHelperFactory.getModelHelper(Product.class);
    private final ModelHelper<ProductVariant> productVariantHelper = ModelHelperFactory.getModelHelper(ProductVariant.class);

    @Override
    public OrderItem createModel(Integer num) {
        Order order = orderHelper.createModel(num);
        Product product = productHelper.createModel(num);
        ProductVariant productVariant = productVariantHelper.createModel(num);

        if (num == 1) {
            return OrderItem.builder()
                    .order(order)
                    .product(product)
                    .productVariant(productVariant)
                    .unitPrice(BigDecimal.valueOf(20.00))
                    .quantity(3)
                    .build();
        } else {
            return OrderItem.builder()
                    .order(order)
                    .product(product)
                    .productVariant(productVariant)
                    .unitPrice(BigDecimal.valueOf(30.00))
                    .quantity(5)
                    .build();
        }
    }
}
