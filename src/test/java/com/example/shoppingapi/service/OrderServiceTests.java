package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Order;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.OrderRepository;
import com.example.shoppingapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = OrderServiceTests.class)
public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    
    private ModelHelper<Order> orderHelper = ModelHelperFactory.getModelHelper(Order.class);
    
    @Test
    public void testGetAllOrders() {
        Order order1 = orderHelper.createModel(1);
        Order order2 = orderHelper.createModel(2);
        List<Order> mockOrders = Arrays.asList(order1, order2);

        when(orderRepository.findAll()).thenReturn(mockOrders);

        List<Order> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testGetOrderById_Found() {
        Order order = orderHelper.createModel(1);
        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));

        Optional<Order> result = orderService.getOrderById(order.getOrderId());

        assertTrue(result.isPresent());
        assertEquals(order.getOrderId(), result.get().getOrderId());
        verify(orderRepository, times(1)).findById(order.getOrderId());
    }

    @Test
    public void testGetOrderById_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Order> result = orderService.getOrderById(1L);

        assertFalse(result.isPresent());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    public void testSaveOrder_Success() {
                
        Order order = orderHelper.createModel(1);

        when(userRepository.findById(order.getUser().getUserId())).thenReturn(Optional.of(order.getUser()));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order createdOrder = orderService.saveOrder(order);

        assertNotNull(createdOrder);
        assertEquals(order.getOrderId(), createdOrder.getOrderId());
        verify(userRepository, times(1)).findById(order.getUser().getUserId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testSaveOrder_MissingUser_ThrowsException() {
        Order order = orderHelper.createModel(1);
        
        order.setUser(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> orderService.saveOrder(order));
        assertEquals("User ID is required to create an order.", ex.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testSaveOrder_MissingProduct_ThrowsException() {
        Order order = orderHelper.createModel(1);
        order.setProduct(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> orderService.saveOrder(order));
        assertEquals("Product ID is required to create an order.", ex.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testUpdateOrder_Success() {
        Order order = orderHelper.createModel(1);


        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));
        when(userRepository.findById(order.getUser().getUserId())).thenReturn(Optional.of(order.getUser()));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        
        Order updatedOrder = order.toBuilder().status("completed").build();

        Order result = orderService.updateOrder(order.getOrderId(), updatedOrder);

        assertNotNull(result);
        assertEquals("completed", result.getStatus());
        verify(orderRepository, times(1)).findById(order.getOrderId());
        verify(orderRepository, times(1)).save(updatedOrder);
    }

    @Test
    public void testPartialUpdateOrder_Success() {
        
        Order order = orderHelper.createModel(1);
        order.setStatus("pending");

        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        
        Map<String, Object> updates = Map.of("status", "shipped");

        Order result = orderService.partialUpdateOrder(order.getOrderId(), updates);

        assertNotNull(result);
        assertEquals("shipped", result.getStatus());
        verify(orderRepository, times(1)).findById(order.getOrderId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testGetOrdersByUserId() {
        Order order = orderHelper.createModel(1);
        User user = order.getUser();
        List<Order> orders = Arrays.asList(order);
        when(orderRepository.findByUserUserId(user.getUserId())).thenReturn(orders);

        List<Order> result = orderService.getOrdersByUserId(user.getUserId());
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByUserUserId(user.getUserId());
    }

    @Test
    public void testGetOrdersByProductId() {
        Order order = orderHelper.createModel(1);
        Product product = order.getProduct();
        List<Order> orders = Arrays.asList(order);
        when(orderRepository.findByProductProductId(product.getProductId())).thenReturn(orders);

        List<Order> result = orderService.getOrdersByProductId(product.getProductId());

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByProductProductId(product.getProductId());
    }

    @Test
    public void testDeleteOrder() {
        Long orderId = 1L;
        orderService.deleteOrder(orderId);
        verify(orderRepository, times(1)).deleteById(orderId);
    }
}
