package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.OrderCreateDTO;
import com.example.shoppingapi.dto.patch.OrderPatchDTO;
import com.example.shoppingapi.dto.put.OrderPutDTO;
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
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

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

    public Order saveOrder(OrderCreateDTO orderCreateDTO) {

        userRepository.findById(orderCreateDTO.getUserId())
            .orElseThrow(() ->
                new ResourceNotFoundException("User not found. Cannot create order."));

        productRepository.findById(orderCreateDTO.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found. Cannot create order."));

        Order order = Order.builder()
                .user(User.builder().userId(orderCreateDTO.getUserId()).build())
                .product(Product.builder().productId(orderCreateDTO.getProductId()).build())
                .build();

        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, OrderPutDTO orderPutDTO) {
        Order existingOrder = getOrderById(id);
        ReflectionUtils.doWithFields(OrderPutDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(orderPutDTO);
            if (value != null) {
                Field orderField = ReflectionUtils.findField(Order.class, field.getName());
                if (orderField != null) {
                    orderField.setAccessible(true);
                    orderField.set(existingOrder, value);
                }
            }
        });
        return orderRepository.save(existingOrder);
    }

    public Order partiallyUpdateOrder(Long id, OrderPatchDTO orderPatchDTO) {
        Order existingOrder = getOrderById(id);
        ReflectionUtils.doWithFields(OrderPatchDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(orderPatchDTO);
            if (value != null) {
                Field orderField = ReflectionUtils.findField(Order.class, field.getName());
                if (orderField != null) {
                    orderField.setAccessible(true);
                    orderField.set(existingOrder, value);
                }
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

}
