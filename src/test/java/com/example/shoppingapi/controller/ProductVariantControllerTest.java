package com.example.shoppingapi.controller;

import com.example.shoppingapi.DotenvLoader;
import com.example.shoppingapi.EntityCreationHelper;
import com.example.shoppingapi.dto.put.ProductVariantPutDTO;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.ProductVariant;
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
import org.springframework.test.web.servlet.MvcResult;

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
public class ProductVariantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityCreationHelper entityCreationHelper;

    @Test
    public void testGetAllProductVariants() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        ProductVariant createdProductVariant = entityCreationHelper.createProductVariant(createdProduct);

        mockMvc.perform(get("/api/productvariants/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched all product variants"))
                .andExpect(jsonPath("$.data[0].productId").value(createdProduct.getProductId()));
    }

    @Test
    public void testGetProductVariantById() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        ProductVariant createdProductVariant = entityCreationHelper.createProductVariant(createdProduct);

        mockMvc.perform(get("/api/productvariants/{id}", createdProductVariant.getVariantId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched product variant"))
                .andExpect(jsonPath("$.data.productId").value(createdProduct.getProductId()))
                .andExpect(jsonPath("$.data.variantName").value(createdProductVariant.getVariantName()))
                .andExpect(jsonPath("$.data.price").value(createdProductVariant.getPrice()))
                .andExpect(jsonPath("$.data.stockQuantity").value(createdProductVariant.getStockQuantity()));
    }

    @Test
    public void testCreateAndFetchProductVariant() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);

        String productVariantJson = "{\n" +
                "  \"variantName\": \"Test Variant\",\n" +
                "  \"productId\": " + createdProduct.getProductId() + ",\n" +
                "  \"price\": 10.00,\n" +
                "  \"stockQuantity\": 100\n" +
                "}";

        // Perform POST request to create product variant
        MvcResult postResult = mockMvc.perform(post("/api/productvariants/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productVariantJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created product variant"))
                .andReturn();

        // Extract the variantId from the POST response
        String responseContent = postResult.getResponse().getContentAsString();
        String variantId = JsonPath.read(responseContent, "$.data.productVariantId").toString();

        mockMvc.perform(get("/api/productvariants/{id}", variantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched product variant"))
                .andExpect(jsonPath("$.data.productId").value(createdProduct.getProductId()))
                .andExpect(jsonPath("$.data.variantName").value("Test Variant"))
                .andExpect(jsonPath("$.data.price").value(10.00))
                .andExpect(jsonPath("$.data.stockQuantity").value(100));
    }


    @Test
    public void testUpdateProductVariant() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        ProductVariant createdProductVariant = entityCreationHelper.createProductVariant(createdProduct);

        String productVariantJson = "{\n" +
                "  \"variantName\": \"Updated Variant\",\n" +
                "  \"price\": 15.00,\n" +
                "  \"stockQuantity\": 150\n" +
                "}";

        mockMvc.perform(put("/api/productvariants/{id}", createdProductVariant.getVariantId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productVariantJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated product variant"));

        mockMvc.perform(get("/api/productvariants/{id}", createdProductVariant.getVariantId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched product variant"))
                .andExpect(jsonPath("$.data.productId").value(createdProduct.getProductId()))
                .andExpect(jsonPath("$.data.variantName").value("Updated Variant"))
                .andExpect(jsonPath("$.data.price").value(15.00))
                .andExpect(jsonPath("$.data.stockQuantity").value(150));
    }

    @Test
    public void testDeleteProductVariant() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        ProductVariant createdProductVariant = entityCreationHelper.createProductVariant(createdProduct);

        mockMvc.perform(delete("/api/productvariants/{id}", createdProductVariant.getVariantId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("Successfully deleted product variant"));
        mockMvc.perform(get("/api/productvariants/{id}", createdProductVariant.getVariantId()))
                .andExpect(status().isNotFound());

    }
}
