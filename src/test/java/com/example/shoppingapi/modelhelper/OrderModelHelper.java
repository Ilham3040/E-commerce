package com.example.shoppingapi.modelhelper;

import com.example.shoppingapi.model.Order;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.model.Product;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class OrderModelHelper implements ModelHelper<Order> {

    private ModelHelper<User> userHelper =  ModelHelperFactory.getModelHelper(User.class);
    private ModelHelper<Product> productHelper =  ModelHelperFactory.getModelHelper(Product.class);

    @Override
    public Order createModel(Integer num) {
        if (num == 1) {
            return Order.builder()
                .orderId(1L)
                .status("pending")
                .orderDate(LocalDateTime.now())
                .user(userHelper.createModel(1))
                .product(productHelper.createModel(1))
                .build();
        } else {
            return Order.builder()
                .orderId(2L)
                .status("completed")
                .orderDate(LocalDateTime.now())
                .user(userHelper.createModel(2))
                .product(productHelper.createModel(2))
                .build();
        }
    }
}
