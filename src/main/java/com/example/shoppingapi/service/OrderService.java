package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Order;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.OrderRepository;
import com.example.shoppingapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Order not found with ID: " + id));
    }

    public Order saveOrder(Order order) {
        Long userId = Optional.ofNullable(order.getUser())
            .map(User::getUserId)
            .orElseThrow(() ->
                new IllegalArgumentException("User ID is required to create an order."));
        Optional.ofNullable(order.getProduct())
            .map(p -> p.getProductId())
            .orElseThrow(() ->
                new IllegalArgumentException("Product ID is required to create an order."));

        userRepository.findById(userId)
            .orElseThrow(() ->
                new ResourceNotFoundException("User not found. Cannot create order."));

        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, Order order) {
        getOrderById(id);  // throws if missing

        Optional.ofNullable(order.getUser())
            .map(User::getUserId)
            .orElseThrow(() ->
                new IllegalArgumentException("User ID is required to update an order."));
        Optional.ofNullable(order.getProduct())
            .map(p -> p.getProductId())
            .orElseThrow(() ->
                new IllegalArgumentException("Product ID is required to update an order."));

        userRepository.findById(order.getUser().getUserId())
            .orElseThrow(() ->
                new ResourceNotFoundException("User not found. Cannot update order."));

        order.setOrderId(id);
        return orderRepository.save(order);
    }

    public Order partialUpdateOrder(Long id, Map<String, Object> updates) {
        Order existing = getOrderById(id);

        BeanWrapper wrapper = new BeanWrapperImpl(existing);
        updates.forEach(wrapper::setPropertyValue);

        return orderRepository.save(existing);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserUserId(userId);
    }

    public List<Order> getOrdersByProductId(Long productId) {
        return orderRepository.findByProductProductId(productId);
    }

    // public void deleteOrder(Long id) {
    //     orderRepository.deleteById(id);
    // }
}
