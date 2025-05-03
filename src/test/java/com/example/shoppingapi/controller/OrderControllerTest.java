package com.example.shoppingapi.controller;

import com.example.shoppingapi.DotenvLoader;
import com.example.shoppingapi.dto.create.OrderCreateDTO;
import com.example.shoppingapi.dto.create.ProductCreateDTO;
import com.example.shoppingapi.dto.create.StoreCreateDTO;
import com.example.shoppingapi.dto.create.UserCreateDTO;
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

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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

    @BeforeAll
    public static void setUp() {
        DotenvLoader.load();
    }

    private User createUser() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername("testuser");
        userCreateDTO.setEmail("testuser" + System.currentTimeMillis() + "@example.com"); // Use a unique email
        userCreateDTO.setPhoneNumber("1234567890");
        return userService.createUser(userCreateDTO);
    }


    // Create a new StoreCreateDTO and associate it with an existing user
    private Store createStore(User user) throws Exception {
        StoreCreateDTO storeCreateDTO = new StoreCreateDTO();
        storeCreateDTO.setStoreName("Test Store");
        storeCreateDTO.setUserId(user.getUserId());

        // Save the store
        return storeService.saveStore(storeCreateDTO);
    }
    // Create a ProductCreateDTO linked to the store's ID
    private Product createProduct(Store store) {
        ProductCreateDTO productCreateDTO = new ProductCreateDTO();
        productCreateDTO.setProductName("Test Product");
        productCreateDTO.setPrice(new BigDecimal("9.99"));
        productCreateDTO.setStoreId(store.getStoreId());
        return productService.saveProduct(productCreateDTO);
    }

    // Create a new OrderCreateDTO linked to the created User and Product
    private OrderCreateDTO createOrderCreateDTO(User user, Product product) {
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO();
        orderCreateDTO.setUserId(user.getUserId());
        orderCreateDTO.setProductId(product.getProductId());
        orderCreateDTO.setStatus("pending");
        return orderCreateDTO;
    }

    private OrderPutDTO createOrderPutDTO() {
        OrderPutDTO orderPutDTO = new OrderPutDTO();
        orderPutDTO.setStatus("completed");
        return orderPutDTO;
    }

    private OrderPatchDTO createOrderPatchDTO() {
        OrderPatchDTO orderPatchDTO = new OrderPatchDTO();
        orderPatchDTO.setStatus("canceled");
        return orderPatchDTO;
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
    @Transactional
    @Rollback(true)
    public void testCreateOrder() throws Exception {
        // Create a user, store, and product
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        Product createdProduct = createProduct(createdStore);

        // Create OrderCreateDTO with the newly created user and product
        OrderCreateDTO orderCreateDTO = createOrderCreateDTO(createdUser, createdProduct);

        String jsonContent = createOrderJson(orderCreateDTO.getUserId(), orderCreateDTO.getProductId(), orderCreateDTO.getStatus());

        // Perform the POST request to create the order
        String responseContent = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Order created"))
                .andReturn().getResponse().getContentAsString();

        // Extract the created order's ID from the response
        Integer createdOrderIdInteger = JsonPath.parse(responseContent).read("$.data.orderId");
        Long createdOrderId = Long.valueOf(createdOrderIdInteger.toString());

        Order createdOrder = orderService.getOrderById(createdOrderId);
        assertEquals(orderCreateDTO.getStatus(), createdOrder.getStatus());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetAllOrders() throws Exception {
        // Create a user, store, and product
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        Product createdProduct = createProduct(createdStore);
        OrderCreateDTO orderCreateDTO = createOrderCreateDTO(createdUser, createdProduct);
        Order savedOrder = orderService.saveOrder(orderCreateDTO);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Fetched all orders"))
                .andExpect(jsonPath("$.data[0].orderId").value(savedOrder.getOrderId()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetOrderById() throws Exception {
        // Create a user, store, product, and order
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        Product createdProduct = createProduct(createdStore);
        OrderCreateDTO orderCreateDTO = createOrderCreateDTO(createdUser, createdProduct);
        Order createdOrder = orderService.saveOrder(orderCreateDTO);

        assertOrderResponse(createdOrder.getOrderId(), orderCreateDTO.getStatus(), createdUser.getUserId(), createdProduct.getProductId());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testUpdateOrder() throws Exception {
        // Create a user, store, product, and order
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        Product createdProduct = createProduct(createdStore);
        OrderCreateDTO orderCreateDTO = createOrderCreateDTO(createdUser, createdProduct);
        Order createdOrder = orderService.saveOrder(orderCreateDTO);

        OrderPutDTO orderPutDTO = createOrderPutDTO();
        String jsonContent = createOrderJson(createdOrder.getUser().getUserId(), createdOrder.getProduct().getProductId(), orderPutDTO.getStatus());

        mockMvc.perform(put("/api/orders/{id}", createdOrder.getOrderId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order updated"));

        assertOrderResponse(createdOrder.getOrderId(), "completed", createdOrder.getUser().getUserId(), createdOrder.getProduct().getProductId());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testPartialUpdateOrder() throws Exception {
        // Create a user, store, product, and order
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        Product createdProduct = createProduct(createdStore);
        OrderCreateDTO orderCreateDTO = createOrderCreateDTO(createdUser, createdProduct);
        Order createdOrder = orderService.saveOrder(orderCreateDTO);

        OrderPatchDTO orderPatchDTO = createOrderPatchDTO();
        String jsonContent = "{ \"status\": \"" + orderPatchDTO.getStatus() + "\" }";

        mockMvc.perform(patch("/api/orders/{id}", createdOrder.getOrderId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order partially updated"));

        assertOrderResponse(createdOrder.getOrderId(), "canceled", createdOrder.getUser().getUserId(), createdOrder.getProduct().getProductId());
    }
}