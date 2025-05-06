package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.OrderItemCreateDTO;
import com.example.shoppingapi.dto.response.OrderItemDTO;
import com.example.shoppingapi.model.Order;
import com.example.shoppingapi.model.OrderItem;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.ProductVariant;
import com.example.shoppingapi.repository.OrderItemRepository;
import com.example.shoppingapi.repository.OrderRepository;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;

    // Fetch all order items
    public List<OrderItemDTO> getAllOrderItems() {
        return orderItemRepository.findAll().stream()
                .map(orderItem -> new OrderItemDTO(
                        orderItem.getOrderItemId(),
                        orderItem.getOrder().getOrderId(),
                        orderItem.getProduct().getProductId(),
                        orderItem.getProductVariant().getVariantId(),
                        orderItem.getUnitPrice(),
                        orderItem.getQuantity(),
                        orderItem.getLineTotal()))
                .collect(Collectors.toList());
    }

    // Fetch order items by order ID
    public List<OrderItemDTO> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderOrderId(orderId).stream()
                .map(orderItem -> new OrderItemDTO(
                        orderItem.getOrderItemId(),
                        orderItem.getOrder().getOrderId(),
                        orderItem.getProduct().getProductId(),
                        orderItem.getProductVariant().getVariantId(),
                        orderItem.getUnitPrice(),
                        orderItem.getQuantity(),
                        orderItem.getLineTotal()))
                .collect(Collectors.toList());
    }

    public OrderItem saveOrderItem(OrderItemCreateDTO orderItemCreateDTO) {
        Order order = orderRepository.findById(orderItemCreateDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderItemCreateDTO.getOrderId()));

        Product product = productRepository.findById(orderItemCreateDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + orderItemCreateDTO.getProductId()));

        ProductVariant productVariant = productVariantRepository.findById(orderItemCreateDTO.getProductVariantId())
                .orElseThrow(() -> new ResourceNotFoundException("ProductVariant not found with ID: " + orderItemCreateDTO.getProductVariantId()));

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .productVariant(productVariant)
                .unitPrice(orderItemCreateDTO.getUnitPrice())
                .quantity(orderItemCreateDTO.getQuantity())
                .build();

        // Save and return the created OrderItem
        return orderItemRepository.save(orderItem);
    }

}
