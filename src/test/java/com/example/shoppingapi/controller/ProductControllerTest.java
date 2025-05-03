package com.example.shoppingapi.controller;

import com.example.shoppingapi.DotenvLoader;
import com.example.shoppingapi.dto.create.ProductCreateDTO;
import com.example.shoppingapi.dto.create.StoreCreateDTO;
import com.example.shoppingapi.dto.create.UserCreateDTO;
import com.example.shoppingapi.dto.put.ProductPutDTO;
import com.example.shoppingapi.dto.patch.ProductPatchDTO;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.StoreRepository;
import com.example.shoppingapi.repository.UserRepository;
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
@Transactional
@Rollback(true)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public static void setUp() {
        DotenvLoader.load();
    }

    // Create a new User for each test
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
    private ProductCreateDTO createProductCreateDTO(Store store) {
        ProductCreateDTO productCreateDTO = new ProductCreateDTO();
        productCreateDTO.setProductName("Test Product");
        productCreateDTO.setPrice(new BigDecimal("9.99"));
        productCreateDTO.setStoreId(store.getStoreId());
        return productCreateDTO;
    }

    private ProductPutDTO createProductPutDTO() {
        ProductPutDTO productPutDTO = new ProductPutDTO();
        productPutDTO.setProductName("Updated Product");
        productPutDTO.setPrice(new BigDecimal("12.99"));
        return productPutDTO;
    }

    private ProductPatchDTO createProductPatchDTO() {
        ProductPatchDTO productPatchDTO = new ProductPatchDTO();
        productPatchDTO.setProductName("Partially Updated Product");
        return productPatchDTO;
    }

    private String createProductJson(String productName, Long storeId, BigDecimal price) {
        return String.format("{ \"productName\": \"%s\", \"storeId\": %d, \"price\": %s }", productName, storeId, price);
    }

    private void assertProductResponse(Long productId, String productName, Long storeId) throws Exception {
        mockMvc.perform(get("/api/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Fetched product"))
                .andExpect(jsonPath("$.data.productId").value(productId))
                .andExpect(jsonPath("$.data.storeId").value(storeId));
    }

    @Test
    public void testCreateProduct() throws Exception {
        // Create a user and store
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);

        // Create ProductCreateDTO with the newly created store
        ProductCreateDTO productCreateDTO = createProductCreateDTO(createdStore);

        String jsonContent = createProductJson(productCreateDTO.getProductName(), productCreateDTO.getStoreId(), productCreateDTO.getPrice());

        // Perform the POST request to create the product
        String responseContent = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Product created"))
                .andReturn().getResponse().getContentAsString();

        // Extract the created product's ID from the response
        Integer createdProductIdInteger = JsonPath.parse(responseContent).read("$.data.productId");

        Long createdProductId = Long.valueOf(createdProductIdInteger.toString());

        Product createdProduct = productService.getProductById(createdProductId);
        assertEquals(productCreateDTO.getProductName(), createdProduct.getProductName());
    }

    @Test
    public void testGetAllProducts() throws Exception {
        // Create a user, store, and product
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        ProductCreateDTO productCreateDTO = createProductCreateDTO(createdStore);
        Product savedProduct = productService.saveProduct(productCreateDTO);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Fetched all products"))
                .andExpect(jsonPath("$.data[0].productId").value(savedProduct.getProductId()));
    }

    @Test
    public void testGetProductById() throws Exception {
        // Create a store and product for testing
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        ProductCreateDTO productCreateDTO = createProductCreateDTO(createdStore);
        Product createdProduct = productService.saveProduct(productCreateDTO);

        assertProductResponse(createdProduct.getProductId(), productCreateDTO.getProductName(), productCreateDTO.getStoreId());
    }

    @Test
    public void testUpdateProduct() throws Exception {
        // Create a product for testing
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        ProductCreateDTO productCreateDTO = createProductCreateDTO(createdStore);
        Product createdProduct = productService.saveProduct(productCreateDTO);

        // Use helper method to create a ProductPutDTO
        ProductPutDTO productPutDTO = createProductPutDTO();

        String jsonContent = createProductJson(productPutDTO.getProductName(), createdProduct.getStore().getStoreId(), productPutDTO.getPrice());

        mockMvc.perform(put("/api/products/{id}", createdProduct.getProductId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product updated"));

        assertProductResponse(createdProduct.getProductId(), "Updated Product", createdProduct.getStore().getStoreId());
    }

    @Test
    public void testPartialUpdateProduct() throws Exception {
        // Create product for testing
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        ProductCreateDTO productCreateDTO = createProductCreateDTO(createdStore);
        Product createdProduct = productService.saveProduct(productCreateDTO);

        // Use helper method to create ProductPatchDTO
        ProductPatchDTO productPatchDTO = createProductPatchDTO();

        String jsonContent = "{ \"productName\": \"" + productPatchDTO.getProductName() + "\" }";

        mockMvc.perform(patch("/api/products/{id}", createdProduct.getProductId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product partially updated"));

        assertProductResponse(createdProduct.getProductId(), "Partially Updated Product", createdProduct.getStore().getStoreId());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        // Create product for testing
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        ProductCreateDTO productCreateDTO = createProductCreateDTO(createdStore);
        Product createdProduct = productService.saveProduct(productCreateDTO);

        mockMvc.perform(delete("/api/products/{id}", createdProduct.getProductId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("Product deleted"));

        assertFalse(productRepository.existsById(createdProduct.getProductId()));
    }
}
