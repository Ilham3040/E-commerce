package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.OrderCreateDTO;
import com.example.shoppingapi.dto.patch.OrderPatchDTO;
import com.example.shoppingapi.dto.put.OrderPutDTO;
import com.example.shoppingapi.model.Order;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.OrderRepository;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private UserRepository  userRepository;
    @Mock private ProductRepository productRepository;
    @InjectMocks private OrderService orderService;

    private final ModelHelper<Order> orderHelper =
        ModelHelperFactory.getModelHelper(Order.class);

    @Test
    void getAllOrders_returnsAllOrders() {
        List<Order> expected = List.of(orderHelper.createModel(1), orderHelper.createModel(2));
        when(orderRepository.findAll()).thenReturn(expected);

        List<Order> actual = orderService.getAllOrders();
        assertEquals(expected, actual);
        verify(orderRepository).findAll();
    }

    @Test
    void getOrderById_found_returnsOrder() {
        Order expected = orderHelper.createModel(1);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(expected));

        Order actual = orderService.getOrderById(1L);
        assertEquals(expected.getOrderId(), actual.getOrderId());
        verify(orderRepository).findById(1L);
    }

    @Test
    void getOrderById_notFound_throwsException() {
        when(orderRepository.findById(2L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> orderService.getOrderById(2L)
        );
        assertEquals("Order not found with ID: 2", ex.getMessage());
        verify(orderRepository).findById(2L);
    }

    @Test
    void saveOrder_withValidOrder_savesAndReturnsOrder() {
        Order order = orderHelper.createModel(1);
        when(userRepository.findById(order.getUser().getUserId())).thenReturn(Optional.of(order.getUser()));
        when(productRepository.findById(order.getProduct().getProductId())).thenReturn(Optional.of(order.getProduct()));
        when(orderRepository.save(any())).thenReturn(order);

        OrderCreateDTO orderCreateDTO = new OrderCreateDTO();
        orderCreateDTO.setUserId(order.getUser().getUserId());
        orderCreateDTO.setProductId(order.getProduct().getProductId());
        orderCreateDTO.setStatus(order.getStatus());

        Order toSave = Order.builder()
                .user(User.builder().userId(orderCreateDTO.getUserId()).build())
                .product(Product.builder().productId(orderCreateDTO.getProductId()).build())
                .status(orderCreateDTO.getStatus())
                .build();

        Order saved = orderService.saveOrder(orderCreateDTO);
        assertEquals(order.getOrderId(), saved.getOrderId());
        verify(userRepository).findById(order.getUser().getUserId());
        verify(productRepository).findById(order.getProduct().getProductId());
        verify(orderRepository).save(toSave);
    }


    @Test
    void updateOrder_existingOrder_savesUpdatedOrder() {
        Order original = orderHelper.createModel(1);

        OrderPutDTO orderPutDTO = new OrderPutDTO();
        orderPutDTO.setStatus("completed");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(original));
        when(orderRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Order result  = orderService.updateOrder(1L, orderPutDTO);

        assertEquals(orderPutDTO.getStatus(), result.getStatus());
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(original);
    }

    @Test
    void partialUpdateOrder_existing_appliesUpdates() {
        Order original = orderHelper.createModel(1);

        OrderPatchDTO orderPatchDTO = new OrderPatchDTO();
        orderPatchDTO.setStatus("shipped");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(original));
        when(orderRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Order result = orderService.partiallyUpdateOrder(1L, orderPatchDTO);

        assertEquals("shipped", result.getStatus());
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(original);
    }

    @Test
    void getOrdersByUserId_returnsOrders() {
        Order o = orderHelper.createModel(1);
        when(orderRepository.findByUserUserId(o.getUser().getUserId()))
            .thenReturn(List.of(o));

        List<Order> result = orderService.getOrdersByUserId(o.getUser().getUserId());
        assertEquals(1, result.size());
        verify(orderRepository).findByUserUserId(o.getUser().getUserId());
    }

    @Test
    void getOrdersByProductId_returnsOrders() {
        Order o = orderHelper.createModel(1);
        when(orderRepository.findByProductProductId(o.getProduct().getProductId()))
            .thenReturn(List.of(o));

        List<Order> result = orderService.getOrdersByProductId(o.getProduct().getProductId());
        assertEquals(1, result.size());
        verify(orderRepository).findByProductProductId(o.getProduct().getProductId());
    }

}
