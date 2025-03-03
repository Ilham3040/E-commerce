package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Order;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.OrderRepository;
import com.example.shoppingapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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


    // Get orders by user ID
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserUserId(userId);
    }

    // Get orders by store ID
    public List<Order> getOrdersByProductId(Long productId) {
        return orderRepository.findByProductProductId(productId);
    }



    // Delete an order by ID
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}

