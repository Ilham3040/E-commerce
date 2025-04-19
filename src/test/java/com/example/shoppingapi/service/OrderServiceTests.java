package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Order;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.OrderRepository;
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
        when(userRepository.findById(order.getUser().getUserId()))
            .thenReturn(Optional.of(order.getUser()));
        when(orderRepository.save(any())).thenReturn(order);

        Order saved = orderService.saveOrder(order);
        assertEquals(order.getOrderId(), saved.getOrderId());
        verify(userRepository).findById(order.getUser().getUserId());
        verify(orderRepository).save(order);
    }

    @Test
    void saveOrder_missingUser_throwsException() {
        Order order = orderHelper.createModel(1);
        order.setUser(null);
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> orderService.saveOrder(order)
        );
        assertEquals("User ID is required to create an order.", ex.getMessage());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void saveOrder_missingProduct_throwsException() {
        Order order = orderHelper.createModel(1);
        order.setProduct(null);
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> orderService.saveOrder(order)
        );
        assertEquals("Product ID is required to create an order.", ex.getMessage());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void updateOrder_existingOrder_savesUpdatedOrder() {
        Order original = orderHelper.createModel(1);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(original));
        when(userRepository.findById(original.getUser().getUserId()))
            .thenReturn(Optional.of(original.getUser()));
        when(orderRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Order updated = original.toBuilder().status("completed").build();
        Order result  = orderService.updateOrder(1L, updated);

        assertEquals("completed", result.getStatus());
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(updated);
    }

    @Test
    void partialUpdateOrder_existing_appliesUpdates() {
        Order existing = orderHelper.createModel(1);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(orderRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Map<String,Object> changes = Map.of("status","shipped");
        Order result = orderService.partialUpdateOrder(1L, changes);

        assertEquals("shipped", result.getStatus());
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(existing);
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

    // @Test
    // void deleteOrder_invokesRepositoryDelete() {
    //     orderService.deleteOrder(1L);
    //     verify(orderRepository).deleteById(1L);
    // }
}
