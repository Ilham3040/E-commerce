package com.example.shoppingapi.controller;

import com.example.shoppingapi.model.Order;
import com.example.shoppingapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.shoppingapi.dto.ApiResponse;
import com.example.shoppingapi.dto.OrderDTO;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUserId(@PathVariable Long userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @GetMapping("/product/{productId}")
    public List<Order> getOrdersByProductId(@PathVariable Long productId) {
        return orderService.getOrdersByProductId(productId);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDTO>> createOrder(@RequestBody Order order) {

        Order savedOrder = orderService.saveOrder(order);

        OrderDTO orderDTO = new OrderDTO(
            savedOrder.getOrderId(),
            savedOrder.getStatus(),
            savedOrder.getOrderDate(),
            savedOrder.getUser().getUserId(),
            savedOrder.getProduct().getProductId()
        );

         ApiResponse<OrderDTO> response = new ApiResponse<>("Order successfully added", orderDTO);
         return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
