package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.request.OrderRequestDTO;
import com.example.shoppingapi.dto.response.OrderDTO;
import com.example.shoppingapi.model.Order;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.OrderRepository;
import com.example.shoppingapi.repository.ProductRepository;
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
    private final ProductRepository productRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Order not found with ID: " + id));
    }

    public Order saveOrder(OrderRequestDTO orderRequestDTO) {

        userRepository.findById(orderRequestDTO.getUserId())
            .orElseThrow(() ->
                new ResourceNotFoundException("User not found. Cannot create order."));

        productRepository.findById(orderRequestDTO.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found. Cannot create order."));

        Order order = Order.builder()
                .user(User.builder().userId(orderRequestDTO.getUserId()).build())
                .product(Product.builder().productId(orderRequestDTO.getProductId()).build())
                .build();

        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, OrderRequestDTO orderRequestDTO) {
        orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found. Cannot create order."));

        userRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found. Cannot create order."));

        productRepository.findById(orderRequestDTO.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found. Cannot create order."));

        Order order = Order.builder()
                .user(User.builder().userId(orderRequestDTO.getUserId()).build())
                .product(Product.builder().productId(orderRequestDTO.getProductId()).build())
                .build();

        return orderRepository.save(order);

    }

    public Order partialUpdateOrder(Long id, Map<String, Object> updates) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found. Cannot update order."));

        BeanWrapper wrapper = new BeanWrapperImpl(existingOrder);
        updates.forEach((key, value) -> {
            if (wrapper.isWritableProperty(key)) {
                wrapper.setPropertyValue(key, value);
            }
        });

        return orderRepository.save(existingOrder);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserUserId(userId);
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found for user ID: " + userId);
        }
        return orders;
    }

    public List<Order> getOrdersByProductId(Long productId) {
        List<Order> orders = orderRepository.findByProductProductId(productId);
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found for product ID: " + productId);
        }
        return orders;
    }


}
