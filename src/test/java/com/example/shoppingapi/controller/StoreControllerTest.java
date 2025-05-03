package com.example.shoppingapi.controller;

import com.example.shoppingapi.DotenvLoader;
import com.example.shoppingapi.EntityCreationHelper;
import com.example.shoppingapi.dto.create.StoreCreateDTO;
import com.example.shoppingapi.dto.create.UserCreateDTO;
import com.example.shoppingapi.dto.put.StorePutDTO;
import com.example.shoppingapi.dto.patch.StorePatchDTO;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.StoreRepository;
import com.example.shoppingapi.repository.UserRepository;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback(true)
public class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

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



    private String createStoreJson(String storeName, Long userId) {
        return String.format("{ \"storeName\": \"%s\", \"userId\": %d }", storeName, userId);
    }



    private void assertStoreResponse(Long storeId, String storeName, Long userId) throws Exception {
        mockMvc.perform(get("/api/stores/{id}", storeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Fetched store"))
                .andExpect(jsonPath("$.data.storeId").value(storeId))
                .andExpect(jsonPath("$.data.userId").value(userId));
    }



    @Test
    public void testCreateStore() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        String jsonContent = createStoreJson("new store", createdUser.getUserId());

        String responseContent = mockMvc.perform(post("/api/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Store created"))
                .andReturn().getResponse().getContentAsString();

        Integer createdStoreIdInteger = JsonPath.parse(responseContent).read("$.data.storeId");
        Long createdStoreId = Long.valueOf(createdStoreIdInteger.toString());

        Store createdStore = storeService.getStoreById(createdStoreId);
        assertEquals("new store", createdStore.getStoreName());
    }



    @Test
    public void testGetAllStores() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);

        mockMvc.perform(get("/api/stores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Fetched all stores"))
                .andExpect(jsonPath("$.data[0].storeId").value(createdStore.getStoreId()));
    }



    @Test
    public void testGetStoreById() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);

        assertStoreResponse(createdStore.getStoreId(), createdStore.getStoreName(), createdStore.getUser().getUserId());
    }



    @Test
    public void testUpdateStore() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);

        String jsonContent = createStoreJson("Updated Store", createdStore.getUser().getUserId());

        mockMvc.perform(put("/api/stores/{id}", createdStore.getStoreId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Store updated"));

        assertStoreResponse(createdStore.getStoreId(), "Updated Store", createdStore.getUser().getUserId());
    }



    @Test
    public void testPartialUpdateStore() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);

        String jsonContent = "{ \"storeName\": \"Partially Updated Store\" }";

        mockMvc.perform(patch("/api/stores/{id}", createdStore.getStoreId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Store partially updated"));

        assertStoreResponse(createdStore.getStoreId(), "Partially Updated Store", createdStore.getUser().getUserId());
    }



    @Test
    public void testDeleteStore() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);

        mockMvc.perform(delete("/api/stores/{id}", createdStore.getStoreId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("Store deleted"));

        assertFalse(storeRepository.existsById(createdStore.getStoreId()));
    }
}
