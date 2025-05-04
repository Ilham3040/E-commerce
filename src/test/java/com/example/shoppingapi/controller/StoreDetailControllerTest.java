package com.example.shoppingapi.controller;

import com.example.shoppingapi.DotenvLoader;
import com.example.shoppingapi.EntityCreationHelper;
import com.example.shoppingapi.dto.create.StoreCreateDTO;
import com.example.shoppingapi.dto.create.StoreDetailCreateDTO;
import com.example.shoppingapi.dto.create.UserCreateDTO;
import com.example.shoppingapi.dto.put.StoreDetailPutDTO;
import com.example.shoppingapi.dto.patch.StoreDetailPatchDTO;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.StoreDetail;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.StoreDetailRepository;
import com.example.shoppingapi.repository.StoreRepository;
import com.example.shoppingapi.repository.UserRepository;
import com.example.shoppingapi.service.StoreDetailService;
import com.example.shoppingapi.service.StoreService;
import com.example.shoppingapi.service.ProductService;
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
public class StoreDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StoreDetailService storeDetailService;

    @Autowired
    private StoreDetailRepository storeDetailRepository;

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
    private EntityCreationHelper entityCreationHelper;

    @BeforeAll
    public static void setUp() {
        DotenvLoader.load();
    }

    private String createStoreDetailJson(Long storeId, String address, String description) {
        return String.format("{ \"storeId\": %d, \"address\": \"%s\", \"description\": \"%s\" }", storeId, address, description);
    }


    @Test
    public void testCreateStoreDetail() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        String jsonContent = createStoreDetailJson(createdStore.getStoreId(), "New Address", "New Desc");

        mockMvc.perform(post("/api/storesdetail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created store detail"));

        StoreDetail createdStoreDetail = storeDetailService.getStoreDetailByStoreId(createdStore.getStoreId());
        assertEquals("New Address", createdStoreDetail.getAddress());
        assertEquals("New Desc", createdStoreDetail.getDescription());
    }

    @Test
    public void testGetAllStoreDetails() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        entityCreationHelper.createStoreDetail(createdStore);

        mockMvc.perform(get("/api/storesdetail"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched all store details"))
                .andExpect(jsonPath("$.data[0].storeId").value(createdStore.getStoreId()));
    }

    @Test
    public void testGetStoreDetailById() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        StoreDetail createdStoreDetail = entityCreationHelper.createStoreDetail(createdStore);

        mockMvc.perform(get("/api/storesdetail/{id}", createdStore.getStoreId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched store detail"))
                .andExpect(jsonPath("$.data.storeDetailId").value(createdStoreDetail.getStoreDetailId()))
                .andExpect(jsonPath("$.data.address").value(createdStoreDetail.getAddress()))
                .andExpect(jsonPath("$.data.description").value(createdStoreDetail.getDescription()))
                .andExpect(jsonPath("$.data.store.storeId").value(createdStoreDetail.getStore().getStoreId()));
    }

    @Test
    public void testUpdateStoreDetail() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        StoreDetail createdStoreDetail = entityCreationHelper.createStoreDetail(createdStore);

        String jsonContent = createStoreDetailJson(createdStore.getStoreId(), "Yugo Road no.22-24", "Updated desc");

        mockMvc.perform(put("/api/storesdetail/{id}", createdStoreDetail.getStore().getStoreId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.message").value("Successfully updated store detail"));

        mockMvc.perform(get("/api/storesdetail/{id}", createdStore.getStoreId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched store detail"))
                .andExpect(jsonPath("$.data.storeDetailId").value(createdStoreDetail.getStoreDetailId()))
                .andExpect(jsonPath("$.data.address").value("Yugo Road no.22-24"))
                .andExpect(jsonPath("$.data.description").value("Updated desc"))
                .andExpect(jsonPath("$.data.store.storeId").value(createdStoreDetail.getStore().getStoreId()));


    }

    @Test
    public void testPartialUpdateStoreDetail() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        StoreDetail createdStoreDetail = entityCreationHelper.createStoreDetail(createdStore);

        String jsonContent = "{ \"description\": \"Updated desc\" }";

        mockMvc.perform(patch("/api/storesdetail/{id}", createdStore.getStoreId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated store detail"));

        mockMvc.perform(get("/api/storesdetail/{id}", createdStore.getStoreId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched store detail"))
                .andExpect(jsonPath("$.data.storeDetailId").value(createdStoreDetail.getStoreDetailId()))
                .andExpect(jsonPath("$.data.address").value(createdStoreDetail.getAddress()))
                .andExpect(jsonPath("$.data.description").value("Updated desc"))
                .andExpect(jsonPath("$.data.store.storeId").value(createdStoreDetail.getStore().getStoreId()));

    }
    @Test
    public void testDeleteStoreDetail() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        StoreDetail createdStoreDetail = entityCreationHelper.createStoreDetail(createdStore);

        mockMvc.perform(delete("/api/storesdetail/{id}", createdStore.getStoreId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("Successfully deleted store detail"));

        assertFalse(storeDetailRepository.existsById(createdStoreDetail.getStoreDetailId()));
    }
}
