package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.create.OrderItemCreateDTO;
import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.OrderItemDTO;
import com.example.shoppingapi.model.OrderItem;
import com.example.shoppingapi.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orderitems/")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping
    public ApiResponse<List<OrderItemDTO>> getAllOrderItems() {
        List<OrderItemDTO> orderItemDTOs = orderItemService.getAllOrderItems();
        return new ApiResponse<>("Successfully fetched all order items", orderItemDTOs, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ApiResponse<List<OrderItemDTO>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItemDTO> orderItemDTOs = orderItemService.getOrderItemsByOrderId(orderId);
        return new ApiResponse<>("Successfully fetched order items by order ID", orderItemDTOs, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<OrderItemDTO> createOrderItem(@RequestBody OrderItemCreateDTO orderItemCreateDTO) {
        OrderItem createdOrderItem = orderItemService.saveOrderItem(orderItemCreateDTO);
        return new ApiResponse<>("Successfully created order item",
                new OrderItemDTO(
                        createdOrderItem.getOrderItemId(),
                        createdOrderItem.getOrder().getOrderId(),
                        createdOrderItem.getProduct().getProductId(),
                        createdOrderItem.getProductVariant().getVariantId(),
                        createdOrderItem.getUnitPrice(),
                        createdOrderItem.getQuantity(),
                        createdOrderItem.getLineTotal()), HttpStatus.CREATED);
    }
}
