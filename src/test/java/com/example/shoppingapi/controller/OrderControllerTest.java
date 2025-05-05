package com.example.shoppingapi.controller;

import com.example.shoppingapi.DotenvLoader;
import com.example.shoppingapi.EntityCreationHelper;
import com.example.shoppingapi.dto.create.OrderCreateDTO;
import com.example.shoppingapi.dto.put.OrderPutDTO;
import com.example.shoppingapi.dto.patch.OrderPatchDTO;
import com.example.shoppingapi.model.Order;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.OrderRepository;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.StoreRepository;
import com.example.shoppingapi.repository.UserRepository;
import com.example.shoppingapi.service.OrderService;
import com.example.shoppingapi.service.ProductService;
import com.example.shoppingapi.service.StoreService;
import com.example.shoppingapi.service.UserService;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback(true)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityCreationHelper entityCreationHelper;

    @BeforeAll
    public static void setUp() {
        DotenvLoader.load();
    }

    private String createOrderJson(Long userId, Long productId, String status) {
        return String.format("{ \"userId\": %d, \"productId\": %d, \"status\": \"%s\" }", userId, productId, status);
    }

    private void assertOrderResponse(Long orderId, String status, Long userId, Long productId) throws Exception {
        mockMvc.perform(get("/api/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Fetched order"))
                .andExpect(jsonPath("$.data.orderId").value(orderId))
                .andExpect(jsonPath("$.data.status").value(status))
                .andExpect(jsonPath("$.data.userId").value(userId))
                .andExpect(jsonPath("$.data.productId").value(productId));
    }

    @Test
    public void testCreateOrder() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);


        String jsonContent = createOrderJson(createdUser.getUserId(), createdProduct.getProductId(), "pending");

        String responseContent = mockMvc.perform(post("/api/orders/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Order created"))
                .andReturn().getResponse().getContentAsString();

        Integer createdOrderIdInteger = JsonPath.parse(responseContent).read("$.data.orderId");
        Long createdOrderId = Long.valueOf(createdOrderIdInteger.toString());

        Order createdOrder = orderService.getOrderById(createdOrderId);
        assertEquals("pending", createdOrder.getStatus());
        assertEquals(createdUser.getUserId(), createdOrder.getUser().getUserId());
        assertEquals(createdProduct.getProductId(), createdOrder.getProduct().getProductId());
    }

    @Test
    public void testGetAllOrders() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        Order createdOrder = entityCreationHelper.createOrder(createdUser,createdProduct);

        mockMvc.perform(get("/api/orders/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Fetched all orders"))
                .andExpect(jsonPath("$.data[0].orderId").value(createdOrder.getOrderId()));
    }

    @Test
    public void testGetOrderById() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        Order createdOrder = entityCreationHelper.createOrder(createdUser,createdProduct);

        assertOrderResponse(createdOrder.getOrderId(), createdOrder.getStatus(), createdUser.getUserId(), createdProduct.getProductId());
    }

    @Test
    public void testUpdateOrder() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        Order createdOrder = entityCreationHelper.createOrder(createdUser,createdProduct);

        String jsonContent = createOrderJson(createdOrder.getUser().getUserId(), createdOrder.getProduct().getProductId(), "completed");

        mockMvc.perform(put("/api/orders/{id}", createdOrder.getOrderId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order updated"));

        assertOrderResponse(createdOrder.getOrderId(), "completed", createdOrder.getUser().getUserId(), createdOrder.getProduct().getProductId());
    }

    @Test
    public void testPartialUpdateOrder() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        Order createdOrder = entityCreationHelper.createOrder(createdUser,createdProduct);

        String jsonContent = "{ \"status\": \"canceled\" }";

        mockMvc.perform(patch("/api/orders/{id}", createdOrder.getOrderId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order partially updated"));

        assertOrderResponse(createdOrder.getOrderId(), "canceled", createdOrder.getUser().getUserId(), createdOrder.getProduct().getProductId());
    }
}