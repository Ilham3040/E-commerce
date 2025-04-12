package com.example.shoppingapi.modelhelper;

import com.example.shoppingapi.model.Order;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.model.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderModelHelper implements ModelHelper<Order> {

    private final Long orderId1 = 1L;
    private final Long orderId2 = 2L;

    public OrderModelHelper() {
    }

    @Override
    public Order createModel(Integer num) {
        if (num == 1) {
            
            User user1 = new User();
            user1.setUserId(1L);
            user1.setUsername("johndoe");
            user1.setEmail("john.doe@example.com");
            user1.setPhoneNumber("1234567890");

            Product product1 = new Product();
            product1.setProductId(1L);
            product1.setProductName("Cat Food");
            product1.setPrice(BigDecimal.valueOf(40000));

            Order order1 = new Order();
            order1.setOrderId(orderId1);
            order1.setUser(user1);
            order1.setProduct(product1);
            order1.setStatus("pending");
            order1.setOrderDate(LocalDateTime.now());
            return order1;
        } else {
            
            User user2 = new User();
            user2.setUserId(2L);
            user2.setUsername("janedoe");
            user2.setEmail("jane.doe@example.com");
            user2.setPhoneNumber("0987654321");

            
            Product product2 = new Product();
            product2.setProductId(2L);
            product2.setProductName("Fish Food");
            product2.setPrice(BigDecimal.valueOf(5000));

            Order order2 = new Order();
            order2.setOrderId(orderId2);
            order2.setUser(user2);
            order2.setProduct(product2);
            order2.setStatus("completed");
            order2.setOrderDate(LocalDateTime.now());
            return order2;
        }
    }
}
