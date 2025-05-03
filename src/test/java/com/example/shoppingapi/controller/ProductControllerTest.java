package com.example.shoppingapi.controller;

import com.example.shoppingapi.DotenvLoader;
import com.example.shoppingapi.EntityCreationHelper;
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

    @Autowired
    private EntityCreationHelper entityCreationHelper;

    @BeforeAll
    public static void setUp() {
        DotenvLoader.load();
    }

    private String createProductJson(String productName, Long storeId) {
        return String.format("{ \"productName\": \"%s\", \"storeId\": %d }", productName, storeId);
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
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);

        String jsonContent = createProductJson("New product", createdStore.getStoreId());

        String responseContent = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Product created"))
                .andReturn().getResponse().getContentAsString();

        Integer createdProductIdInteger = JsonPath.parse(responseContent).read("$.data.productId");

        Long createdProductId = Long.valueOf(createdProductIdInteger.toString());

        Product createdProduct = productService.getProductById(createdProductId);
        assertEquals("New product", createdProduct.getProductName());
    }

    @Test
    public void testGetAllProducts() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Fetched all products"))
                .andExpect(jsonPath("$.data[0].productId").value(createdProduct.getProductId()));
    }

    @Test
    public void testGetProductById() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);

        assertProductResponse(createdProduct.getProductId(), createdProduct.getProductName(), createdProduct.getStore().getStoreId());
    }

    @Test
    public void testUpdateProduct() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);

        String jsonContent = createProductJson("Updated Product", createdProduct.getStore().getStoreId());

        mockMvc.perform(put("/api/products/{id}", createdProduct.getProductId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product updated"));

        assertProductResponse(createdProduct.getProductId(), "Updated Product", createdProduct.getStore().getStoreId());
    }

    @Test
    public void testPartialUpdateProduct() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);

        String jsonContent = "{ \"productName\": \"Partially Updated Product\" }";

        mockMvc.perform(patch("/api/products/{id}", createdProduct.getProductId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product partially updated"));

        assertProductResponse(createdProduct.getProductId(), "Partially Updated Product", createdProduct.getStore().getStoreId());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);

        mockMvc.perform(delete("/api/products/{id}", createdProduct.getProductId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("Product deleted"));

        assertFalse(productRepository.existsById(createdProduct.getProductId()));
    }
}
