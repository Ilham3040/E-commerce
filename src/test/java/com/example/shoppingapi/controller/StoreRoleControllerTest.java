package com.example.shoppingapi.controller;

import com.example.shoppingapi.DotenvLoader;
import com.example.shoppingapi.EntityCreationHelper;
import com.example.shoppingapi.dto.create.StoreRoleCreateDTO;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.StoreRole;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.StoreRoleRepository;
import com.example.shoppingapi.service.StoreRoleService;
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
public class StoreRoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StoreRoleService storeRoleService;

    @Autowired
    private StoreRoleRepository storeRoleRepository;

    @Autowired
    private EntityCreationHelper entityCreationHelper;

    @BeforeAll
    public static void setUp() {
        DotenvLoader.load();
    }

    private String createStoreRoleJson(Long storeId, Long userId) {
        return String.format("{ \"storeId\": %d, \"userId\": \"%d\"}", storeId, userId);
    }

    @Test
    public void testCreateStoreRole() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);

        String jsonContent = createStoreRoleJson(createdStore.getStoreId(), createdUser.getUserId());

        mockMvc.perform(post("/api/storeroles/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created store role"))
                .andExpect(jsonPath("$.data.storeId").value(createdStore.getStoreId()))
                .andExpect(jsonPath("$.data.userId").value(createdUser.getUserId()))
                .andExpect(jsonPath("$.data.role").value("admin"));
    }

    @Test
    public void testGetAllStoreRolesByStoreId() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        entityCreationHelper.createStoreRole(createdStore, createdUser);

        mockMvc.perform(get("/api/storeroles/store/{storeId}", createdStore.getStoreId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched all store roles by store ID"))
                .andExpect(jsonPath("$.data[0].storeId").value(createdStore.getStoreId()))
                .andExpect(jsonPath("$.data[0].userId").value(createdUser.getUserId()))
                .andExpect(jsonPath("$.data[0].role").value("admin"));
    }

    @Test
    public void testGetAllStoreRolesByUserId() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        entityCreationHelper.createStoreRole(createdStore, createdUser);

        mockMvc.perform(get("/api/storeroles/user/{userId}", createdUser.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched all store roles by user ID"))
                .andExpect(jsonPath("$.data[0].storeId").value(createdStore.getStoreId()))
                .andExpect(jsonPath("$.data[0].userId").value(createdUser.getUserId()))
                .andExpect(jsonPath("$.data[0].role").value("admin"));;
    }

    @Test
    public void testDeleteStoreRole() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        StoreRole storeRole = entityCreationHelper.createStoreRole(createdStore, createdUser);

        mockMvc.perform(delete("/api/storeroles/{storeId}/{userId}", createdStore.getStoreId(), createdUser.getUserId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("Successfully deleted store role"));

        assertEquals(0, storeRoleRepository.count());
    }
}
