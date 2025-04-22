//package com.example.shoppingapi.controller;
//
//import com.example.shoppingapi.dto.request.OrderRequestDTO;
//import com.example.shoppingapi.dto.response.OrderDTO;
//import com.example.shoppingapi.model.*;
//import com.example.shoppingapi.dto.response.ApiResponse;
//
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import com.example.shoppingapi.service.OrderService;;
//
//@RestController
//@RequestMapping("/api/orders")
//@RequiredArgsConstructor
//public class OrderController {
//    private final OrderService orderService;
//
//    @GetMapping
//    public ApiResponse<List<OrderDTO>> getAllOrders(){
//        List<OrderDTO> datas = orderService.getAllOrders()
//            .stream()
//            .map(order -> new OrderDTO(order.getOrderId(),order.getUser().getUserId(),order.getProduct().getProductId()))
//            .collect(Collectors.toList());
//        return new ApiResponse<>("Fetched all order datas", datas,HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ApiResponse<Order> getOrderById(@PathVariable Long id){
//        Order data = orderService.getOrderById(id);
//        return new ApiResponse<>("Fetched order data", data,HttpStatus.OK);
//    }
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public ApiResponse<OrderDTO> createOrder(@Validated @RequestBody OrderRequestDTO dto){
//        Order wannabe = Order.builder()
//            .user(User.builder().userId(dto.getUserId()).build())
//            .product(Product.builder().productId(dto.getProductId()).build())
//            .build();
//
//        Order created = orderService.saveOrder(wannabe);
//        return new ApiResponse<>("Sucessfully creating order",
//            new OrderDTO(created.getOrderId(),
//                        created.getUser().getUserId(),
//                        created.getProduct().getProductId()),HttpStatus.CREATED);
//
//    }
//
//    @PutMapping("/{id}")
//    public ApiResponse<OrderDTO> updateOrder(
//        @PathVariable Long id,
//        @Validated @RequestBody OrderRequestDTO dto
//        )
//    {
//        Order wannabe = Order.builder()
//            .orderId(id)
//            .user(User.builder().userId(dto.getUserId()).build())
//            .product(Product.builder().productId(dto.getProductId()).build())
//            .build();
//
//        Order updated = orderService.updateOrder(id, wannabe);
//        return new ApiResponse<>("Sucessfully update ",
//            new OrderDTO(updated.getOrderId(),
//                        updated.getUser().getUserId(),
//                        updated.getProduct().getProductId()),HttpStatus.OK);
//    }
//
//    @PatchMapping("/{id}")
//    public ApiResponse<OrderDTO> patchOrder(
//        @PathVariable Long id,
//        @RequestBody Map<String,Object> updates
//    ){
//        Order updated = orderService.partialUpdateOrder(id, updates);
//        return new ApiResponse<>("Sucessfully update ",
//            new OrderDTO(updated.getOrderId(),
//                        updated.getUser().getUserId(),
//                        updated.getProduct().getProductId()),HttpStatus.OK);
//    }
//
//}
