package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Order;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.OrderRepository;
import com.example.shoppingapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;



import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository usersRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order saveOrder(Order order) {
        if (order.getUser() == null || order.getUser().getUserId() == null) {
            throw new IllegalArgumentException("User ID is required to create an order.");
        }

        if (order.getProduct() == null || order.getProduct().getProductId() == null) {
            throw new IllegalArgumentException("Product ID is required to create an order.");
        }

        Optional<User> user = usersRepository.findById(order.getUser().getUserId());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found. Cannot create order.");
        }

        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, Order order) {
        Optional<Order> existingOrderOpt = orderRepository.findById(id);
        if (existingOrderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order not found with ID: " + id);
        }
        if (order.getUser() == null || order.getUser().getUserId() == null) {
            throw new IllegalArgumentException("User ID is required to create an order.");
        }

        if (order.getProduct() == null || order.getProduct().getProductId() == null) {
            throw new IllegalArgumentException("Product ID is required to create an order.");
        }

        Optional<User> user = usersRepository.findById(order.getUser().getUserId());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found. Cannot create order.");
        }

        Order updatedOrder = order;
        updatedOrder.setOrderId(id);

        return orderRepository.save(updatedOrder);
    }

    
    public Order partialUpdateOrder(Long id, Map<String, Object> updates) {
        Optional<Order> existingOrderOpt = orderRepository.findById(id);
        if (existingOrderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order not found with ID: " + id);
        }
        
        Order existingOrder = existingOrderOpt.get();
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Order.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, existingOrder, value);
            }
        });

        return orderRepository.save(existingOrder);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserUserId(userId);
    }

    public List<Order> getOrdersByProductId(Long productId) {
        return orderRepository.findByProductProductId(productId);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}

