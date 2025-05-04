package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.create.OrderCreateDTO;
import com.example.shoppingapi.dto.patch.OrderPatchDTO;
import com.example.shoppingapi.dto.put.OrderPutDTO;
import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.OrderDTO;
import com.example.shoppingapi.model.Order;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.service.OrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ApiResponse<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orderDTOs = orderService.getAllOrders()
                .stream()
                .map(order -> new OrderDTO(order.getOrderId(), order.getUser().getUserId(), order.getProduct().getProductId(),order.getStatus()))
                .collect(Collectors.toList());
        return new ApiResponse<>("Fetched all orders", orderDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderDTO> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return new ApiResponse<>("Fetched order", new OrderDTO(order.getOrderId(), order.getUser().getUserId(), order.getProduct().getProductId(), order.getStatus()), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<OrderDTO> createOrder(@Validated @RequestBody OrderCreateDTO orderCreateDTO) {
        Order createdOrder = orderService.saveOrder(orderCreateDTO);
        return new ApiResponse<>("Order created", new OrderDTO(createdOrder.getOrderId(), createdOrder.getUser().getUserId(), createdOrder.getProduct().getProductId(),createdOrder.getStatus()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ApiResponse<OrderDTO> updateOrder(@PathVariable Long id, @Validated @RequestBody OrderPutDTO orderPutDTO) {
        Order updatedOrder = orderService.updateOrder(id, orderPutDTO);
        return new ApiResponse<>("Order updated", new OrderDTO(updatedOrder.getOrderId(), updatedOrder.getUser().getUserId(), updatedOrder.getProduct().getProductId(),updatedOrder.getStatus()), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ApiResponse<OrderDTO> partialUpdateOrder(@PathVariable Long id, @RequestBody OrderPatchDTO orderPatchDTO) {
        Order updatedOrder = orderService.partiallyUpdateOrder(id, orderPatchDTO);
        return new ApiResponse<>("Order partially updated", new OrderDTO(updatedOrder.getOrderId(), updatedOrder.getUser().getUserId(), updatedOrder.getProduct().getProductId(),updatedOrder.getStatus()), HttpStatus.OK);
    }
}
