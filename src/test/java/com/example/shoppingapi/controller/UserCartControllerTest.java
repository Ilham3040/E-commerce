package com.example.shoppingapi.controller;

import com.example.shoppingapi.DotenvLoader;
import com.example.shoppingapi.EntityCreationHelper;
import com.example.shoppingapi.dto.create.UserCartCreateDTO;
import com.example.shoppingapi.dto.create.UserFavoriteCreateDTO;
import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.ProductDTO;
import com.example.shoppingapi.dto.response.UserCartDTO;
import com.example.shoppingapi.dto.response.UserFavoriteDTO;
import com.example.shoppingapi.model.*;
import com.example.shoppingapi.repository.UserCartRepository;
import com.example.shoppingapi.service.UserCartService;
import com.example.shoppingapi.service.UserFavoriteService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback(true)
public class UserCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserCartService userCartService;

    @Autowired
    private UserCartRepository userCartRepository;

    @Autowired
    private EntityCreationHelper entityCreationHelper;

    @BeforeAll
    public static void setUp() {
        DotenvLoader.load();
    }

    private String createUserCartJson(Long userId, Long productId) {
        return String.format("{ \"userId\": %d, \"productId\": %d }", userId, productId);
    }

    private void assertUserCartResponse(Long userId, Long productId) throws Exception {
        mockMvc.perform(get("/api/usercart/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched all user carts"))
                .andExpect(jsonPath("$.data[0].userId").value(userId))
                .andExpect(jsonPath("$.data[0].productId").value(productId));
    }

    @Test
    public void testAddToUserCart() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);

        String jsonContent = createUserCartJson(createdUser.getUserId(), createdProduct.getProductId());

        mockMvc.perform(post("/api/usercart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully added product to user cart"))
                .andExpect(jsonPath("$.data.userId").value(createdUser.getUserId()))
                .andExpect(jsonPath("$.data.productId").value(createdProduct.getProductId()));
    }

    @Test
    public void testGetUserCartsByUserId() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        entityCreationHelper.createUserCart(createdUser, createdProduct);

        mockMvc.perform(get("/api/usercart/user/{userId}", createdUser.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched all user carts"))
                .andExpect(jsonPath("$.data[0].userId").value(createdUser.getUserId()))
                .andExpect(jsonPath("$.data[0].productId").value(createdProduct.getProductId()));
    }

    @Test
    public void testDeleteUserCart() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        UserCart createdUserCart = entityCreationHelper.createUserCart(createdUser, createdProduct);

        mockMvc.perform(delete("/api/usercart/{userId}/{productId}", createdUser.getUserId(),createdProduct.getProductId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("Successfully removed product from user cart"));

        assertFalse(userCartRepository.existsById(createdUserCart.getId()));
    }
}