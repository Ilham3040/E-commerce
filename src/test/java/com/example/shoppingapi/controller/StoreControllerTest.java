package com.example.shoppingapi.controller;

import com.example.shoppingapi.DotenvLoader;
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

    // Create a StoreCreateDTO with the user's ID
    private StoreCreateDTO createStoreCreateDTO(User user) {
        StoreCreateDTO storeCreateDTO = new StoreCreateDTO();
        storeCreateDTO.setStoreName("Test Store");
        storeCreateDTO.setUserId(user.getUserId()); // Associate with the created user
        return storeCreateDTO;
    }

    private StorePutDTO createStorePutDTO() {
        StorePutDTO storePutDTO = new StorePutDTO();
        storePutDTO.setStoreName("Updated Store");
        return storePutDTO;
    }

    private StorePatchDTO createStorePatchDTO() {
        StorePatchDTO storePatchDTO = new StorePatchDTO();
        storePatchDTO.setStoreName("Partially Updated Store");
        return storePatchDTO;
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
    @Transactional
    @Rollback(true)
    public void testCreateStore() throws Exception {
        // Create a new user
        User createdUser = createUser();

        // Create StoreCreateDTO with the newly created user
        StoreCreateDTO storeCreateDTO = createStoreCreateDTO(createdUser);

        String jsonContent = createStoreJson(storeCreateDTO.getStoreName(), storeCreateDTO.getUserId());

        // Perform the POST request to create the store
        String responseContent = mockMvc.perform(post("/api/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Store created"))
                .andReturn().getResponse().getContentAsString();

        // Extract the created store's ID from the response
        Integer createdStoreIdInteger = JsonPath.parse(responseContent).read("$.data.storeId");

        Long createdStoreId = Long.valueOf(createdStoreIdInteger.toString());

        Store createdStore = storeService.getStoreById(createdStoreId);
        assertEquals(storeCreateDTO.getStoreName(), createdStore.getStoreName());
    }


    @Test
    @Transactional
    @Rollback(true)
    public void testGetAllStores() throws Exception {
        // Create a new user and store
        User createdUser = createUser();
        StoreCreateDTO storeCreateDTO = createStoreCreateDTO(createdUser);
        Store savedStore = storeService.saveStore(storeCreateDTO);

        mockMvc.perform(get("/api/stores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Fetched all stores"))
                .andExpect(jsonPath("$.data[0].storeId").value(savedStore.getStoreId()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetStoreById() throws Exception {
        // Create a store and retrieve it for testing
        User createdUser = createUser();
        StoreCreateDTO storeCreateDTO = createStoreCreateDTO(createdUser);
        Store createdStore = storeService.saveStore(storeCreateDTO);

        assertStoreResponse(createdStore.getStoreId(), storeCreateDTO.getStoreName(), storeCreateDTO.getUserId());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testUpdateStore() throws Exception {
        // Create a store for testing
        User createdUser = createUser();
        StoreCreateDTO storeCreateDTO = createStoreCreateDTO(createdUser);
        Store createdStore = storeService.saveStore(storeCreateDTO);

        // Use helper method to create a StorePutDTO
        StorePutDTO storePutDTO = createStorePutDTO();
        storePutDTO.setStoreName("Updated Store");

        String jsonContent = createStoreJson(storePutDTO.getStoreName(), createdStore.getUser().getUserId());

        mockMvc.perform(put("/api/stores/{id}", createdStore.getStoreId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Store updated"));

        assertStoreResponse(createdStore.getStoreId(), "Updated Store", createdStore.getUser().getUserId());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testPartialUpdateStore() throws Exception {
        // Create store for testing
        User createdUser = createUser();
        StoreCreateDTO storeCreateDTO = createStoreCreateDTO(createdUser);
        Store createdStore = storeService.saveStore(storeCreateDTO);

        // Use helper method to create StorePatchDTO
        StorePatchDTO storePatchDTO = createStorePatchDTO();
        storePatchDTO.setStoreName("Partially Updated Store");

        String jsonContent = "{ \"storeName\": \"" + storePatchDTO.getStoreName() + "\" }";

        mockMvc.perform(patch("/api/stores/{id}", createdStore.getStoreId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Store partially updated"));

        assertStoreResponse(createdStore.getStoreId(), "Partially Updated Store", createdStore.getUser().getUserId());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testDeleteStore() throws Exception {
        // Create store for testing
        User createdUser = createUser();
        StoreCreateDTO storeCreateDTO = createStoreCreateDTO(createdUser);
        Store createdStore = storeService.saveStore(storeCreateDTO);

        mockMvc.perform(delete("/api/stores/{id}", createdStore.getStoreId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("Store deleted"));

        assertFalse(storeRepository.existsById(createdStore.getStoreId()));
    }
}
