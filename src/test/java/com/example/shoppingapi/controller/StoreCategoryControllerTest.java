package com.example.shoppingapi.controller;

import com.example.shoppingapi.DotenvLoader;
import com.example.shoppingapi.EntityCreationHelper;
import com.example.shoppingapi.dto.create.StoreCategoryCreateDTO;
import com.example.shoppingapi.dto.create.StoreCreateDTO;
import com.example.shoppingapi.dto.create.UserCreateDTO;
import com.example.shoppingapi.dto.put.StoreCategoryPutDTO;
import com.example.shoppingapi.dto.patch.StoreCategoryPatchDTO;
import com.example.shoppingapi.dto.response.StoreCategoryDTO;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.StoreCategory;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.service.StoreCategoryService;
import com.example.shoppingapi.service.StoreService;
import com.example.shoppingapi.service.UserService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback(true)
public class StoreCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private StoreCategoryService storeCategoryService;

    @Autowired
    private EntityCreationHelper entityCreationHelper;

    @BeforeAll
    public static void setUp() {
        DotenvLoader.load();
    }


    @Test
    public void testCreateStoreCategory() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        StoreCategoryCreateDTO storeCategoryCreateDTO = new StoreCategoryCreateDTO();
        storeCategoryCreateDTO.setCategoryName("Category 1");
        storeCategoryCreateDTO.setStoreId(createdStore.getStoreId());

        mockMvc.perform(post("/api/storecategories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"storeId\": " + createdStore.getStoreId() + ", \"categoryName\": \"Category 1\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created store category"))
                .andExpect(jsonPath("$.data.storeId").value(createdStore.getStoreId()))
                .andExpect(jsonPath("$.data.categoryId").exists());
    }

    @Test
    public void testGetAllStoreCategories() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        entityCreationHelper.createStoreCategory(createdStore);

        mockMvc.perform(get("/api/storecategories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched all store categories"))
                .andExpect(jsonPath("$.data[0].storeId").value(createdStore.getStoreId()));
    }

    @Test
    public void testGetStoreCategoryById() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        StoreCategory storeCategory = entityCreationHelper.createStoreCategory(createdStore);

        mockMvc.perform(get("/api/storecategories/{id}", storeCategory.getCategoryId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched store category"))
                .andExpect(jsonPath("$.data.store.storeId").value(createdStore.getStoreId()))
                .andExpect(jsonPath("$.data.categoryId").value(storeCategory.getCategoryId()))
                .andExpect(jsonPath("$.data.categoryName").value("new category"));
    }

    @Test
    public void testGetCategoriesByStoreId() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        entityCreationHelper.createStoreCategory(createdStore);

        mockMvc.perform(get("/api/storecategories/store/{storeId}", createdStore.getStoreId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched store categories by store ID"))
                .andExpect(jsonPath("$.data[0].storeId").value(createdStore.getStoreId()));
    }

    @Test
    public void testUpdateStoreCategory() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        StoreCategory storeCategory = entityCreationHelper.createStoreCategory(createdStore);

        mockMvc.perform(put("/api/storecategories/{id}", storeCategory.getCategoryId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"categoryName\": \"Updated Category\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated store category"))
                .andExpect(jsonPath("$.data.categoryId").value(storeCategory.getCategoryId()));

        mockMvc.perform(get("/api/storecategories/{id}", storeCategory.getCategoryId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched store category"))
                .andExpect(jsonPath("$.data.store.storeId").value(createdStore.getStoreId()))
                .andExpect(jsonPath("$.data.categoryId").value(storeCategory.getCategoryId()))
                .andExpect(jsonPath("$.data.categoryName").value("Updated Category"));

    }

    @Test
    public void testDeleteStoreCategory() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        StoreCategory storeCategory = entityCreationHelper.createStoreCategory(createdStore);

        mockMvc.perform(delete("/api/storecategories/{id}", storeCategory.getCategoryId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("Successfully deleted store category"));
    }
}
