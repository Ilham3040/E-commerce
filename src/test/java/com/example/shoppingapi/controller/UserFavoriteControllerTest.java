package com.example.shoppingapi.controller;

import com.example.shoppingapi.DotenvLoader;
import com.example.shoppingapi.EntityCreationHelper;
import com.example.shoppingapi.model.*;
import com.example.shoppingapi.repository.UserFavoriteRepository;
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
public class UserFavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserFavoriteService userFavoriteService;

    @Autowired
    private UserFavoriteRepository userFavoriteRepository;

    @Autowired
    private EntityCreationHelper entityCreationHelper;

    @BeforeAll
    public static void setUp() {
        DotenvLoader.load();
    }

    private String createUserFavoriteJson(Long userId, Long productId) {
        return String.format("{ \"userId\": %d, \"productId\": %d }", userId, productId);
    }

    @Test
    public void testAddToUserFavorites() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);

        String jsonContent = createUserFavoriteJson(createdUser.getUserId(), createdProduct.getProductId());

        mockMvc.perform(post("/api/userfavorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully added product to user favorites"))
                .andExpect(jsonPath("$.data.userId").value(createdUser.getUserId()))
                .andExpect(jsonPath("$.data.productId").value(createdProduct.getProductId()));
    }

    @Test
    public void testGetUserFavoritesByUserId() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        entityCreationHelper.createUserFavorite(createdUser, createdProduct);

        mockMvc.perform(get("/api/userfavorites/user/{userId}", createdUser.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched all user carts"))
                .andExpect(jsonPath("$.data[0].userId").value(createdUser.getUserId()))
                .andExpect(jsonPath("$.data[0].productId").value(createdProduct.getProductId()));
    }

    @Test
    public void testDeleteUserFavorite() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        UserFavorite createdUserFavorite = entityCreationHelper.createUserFavorite(createdUser, createdProduct);

        mockMvc.perform(delete("/api/userfavorites/{userId}/{productId}", createdUser.getUserId(),createdProduct.getProductId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("Successfully removed product from user favorites"));

        assertFalse(userFavoriteRepository.existsById(createdUserFavorite.getId()));
    }
}
