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
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {

    @Mock private OrderItemRepository orderItemRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private ProductRepository productRepository;
    @Mock private ProductVariantRepository productVariantRepository;

    @InjectMocks private OrderItemService orderItemService;

    private final ModelHelper<OrderItem> orderItemHelper = ModelHelperFactory.getModelHelper(OrderItem.class);
    private final ModelHelper<Product> productHelper = ModelHelperFactory.getModelHelper(Product.class);
    private final ModelHelper<ProductVariant> productVariantHelper = ModelHelperFactory.getModelHelper(ProductVariant.class);

    @Test
    void getAllOrderItems_returnsAllOrderItems() {
        List<OrderItem> expected = List.of(orderItemHelper.createModel(1), orderItemHelper.createModel(2));
        when(orderItemRepository.findAll()).thenReturn(expected);

        List<OrderItemDTO> actual = orderItemService.getAllOrderItems();
        assertEquals(expected.size(), actual.size());
        verify(orderItemRepository).findAll();
    }

    @Test
    void getOrderItemsByOrderId_returnsOrderItems() {
        OrderItem expected = orderItemHelper.createModel(1);
        when(orderItemRepository.findByOrderOrderId(1L)).thenReturn(List.of(expected));

        List<OrderItemDTO> actual = orderItemService.getOrderItemsByOrderId(1L);
        assertEquals(1, actual.size());
        assertEquals(expected.getOrder().getOrderId(), actual.getFirst().getOrderId());
        verify(orderItemRepository).findByOrderOrderId(1L);
    }

    @Test
    void saveOrderItem_createsAndReturnsOrderItem() {
        Product product = productHelper.createModel(1);
        ProductVariant productVariant = productVariantHelper.createModel(1);

        OrderItemCreateDTO orderItemCreateDTO = new OrderItemCreateDTO();
        orderItemCreateDTO.setOrderId(1L);
        orderItemCreateDTO.setProductId(product.getProductId());
        orderItemCreateDTO.setProductVariantId(productVariant.getVariantId());
        orderItemCreateDTO.setUnitPrice(BigDecimal.valueOf(10.99));
        orderItemCreateDTO.setQuantity(2);

        OrderItem savedOrderItem = OrderItem.builder()
                .order(Order.builder().orderId(orderItemCreateDTO.getOrderId()).build())
                .product(product)
                .productVariant(productVariant)
                .unitPrice(orderItemCreateDTO.getUnitPrice())
                .quantity(orderItemCreateDTO.getQuantity())
                .build();

        when(orderRepository.findById(orderItemCreateDTO.getOrderId())).thenReturn(Optional.of(Order.builder().orderId(orderItemCreateDTO.getOrderId()).build()));
        when(productRepository.findById(orderItemCreateDTO.getProductId())).thenReturn(Optional.of(product));
        when(productVariantRepository.findById(orderItemCreateDTO.getProductVariantId())).thenReturn(Optional.of(productVariant));
        when(orderItemRepository.save(any())).thenReturn(savedOrderItem);

        OrderItem result = orderItemService.saveOrderItem(orderItemCreateDTO);
        assertEquals(savedOrderItem.getOrder().getOrderId(), result.getOrder().getOrderId());
        assertEquals(savedOrderItem.getProduct().getProductId(), result.getProduct().getProductId());
        verify(orderItemRepository).save(any());
    }
}
