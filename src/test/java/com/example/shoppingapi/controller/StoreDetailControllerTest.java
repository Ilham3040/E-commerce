package com.example.shoppingapi.controller;

import com.example.shoppingapi.DotenvLoader;
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

    @BeforeAll
    public static void setUp() {
        DotenvLoader.load();
    }

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

    private StoreDetailCreateDTO createStoreDetailCreateDTO(Store store) {
        StoreDetailCreateDTO storeDetailCreateDTO = new StoreDetailCreateDTO();
        storeDetailCreateDTO.setStoreId(store.getStoreId());
        storeDetailCreateDTO.setAddress("Test Address");
        storeDetailCreateDTO.setDescription("Test Description");
        return storeDetailCreateDTO;
    }

    private StoreDetailPutDTO createStoreDetailPutDTO() {
        StoreDetailPutDTO storeDetailPutDTO = new StoreDetailPutDTO();
        storeDetailPutDTO.setAddress("Updated Address");
        storeDetailPutDTO.setDescription("Updated Description");
        return storeDetailPutDTO;
    }

    private StoreDetailPatchDTO createStoreDetailPatchDTO() {
        StoreDetailPatchDTO storeDetailPatchDTO = new StoreDetailPatchDTO();
        storeDetailPatchDTO.setAddress("Partially Updated Address");
        return storeDetailPatchDTO;
    }

    private String createStoreDetailJson(Long storeId, String address, String description) {
        return String.format("{ \"storeId\": %d, \"address\": \"%s\", \"description\": \"%s\" }", storeId, address, description);
    }

    private void assertStoreDetailResponse(Long storeDetailId, String address, String description, Long storeId) throws Exception {
        mockMvc.perform(get("/api/storesdetail/{id}", storeDetailId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched store detail"))
                .andExpect(jsonPath("$.data.storeDetailId").value(storeDetailId))
                .andExpect(jsonPath("$.data.address").value(address))
                .andExpect(jsonPath("$.data.description").value(description))
                .andExpect(jsonPath("$.data.storeId").value(storeId));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testCreateStoreDetail() throws Exception {
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);

        StoreDetailCreateDTO storeDetailCreateDTO = createStoreDetailCreateDTO(createdStore);

        String jsonContent = createStoreDetailJson(storeDetailCreateDTO.getStoreId(), storeDetailCreateDTO.getAddress(), storeDetailCreateDTO.getDescription());

        String responseContent = mockMvc.perform(post("/api/storesdetail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created store detail"))
                .andReturn().getResponse().getContentAsString();

        Integer createdStoreDetailIdInteger = JsonPath.parse(responseContent).read("$.data.storeDetailId");
        Long createdStoreDetailId = Long.valueOf(createdStoreDetailIdInteger.toString());

        StoreDetail createdStoreDetail = storeDetailService.getStoreDetailByStoreId(createdStore.getStoreId());
        assertEquals(storeDetailCreateDTO.getAddress(), createdStoreDetail.getAddress());
        assertEquals(storeDetailCreateDTO.getDescription(), createdStoreDetail.getDescription());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetAllStoreDetails() throws Exception {
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        StoreDetailCreateDTO storeDetailCreateDTO = createStoreDetailCreateDTO(createdStore);
        storeDetailService.saveStoreDetail(storeDetailCreateDTO);

        mockMvc.perform(get("/api/storesdetail"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched all store details"))
                .andExpect(jsonPath("$.data[0].storeId").value(createdStore.getStoreId()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetStoreDetailById() throws Exception {
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        StoreDetailCreateDTO storeDetailCreateDTO = createStoreDetailCreateDTO(createdStore);
        StoreDetail createdStoreDetail = storeDetailService.saveStoreDetail(storeDetailCreateDTO);

        assertStoreDetailResponse(createdStoreDetail.getStoreDetailId(), storeDetailCreateDTO.getAddress(), storeDetailCreateDTO.getDescription(), createdStore.getStoreId());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testUpdateStoreDetail() throws Exception {
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        StoreDetailCreateDTO storeDetailCreateDTO = createStoreDetailCreateDTO(createdStore);
        StoreDetail createdStoreDetail = storeDetailService.saveStoreDetail(storeDetailCreateDTO);

        StoreDetailPutDTO storeDetailPutDTO = createStoreDetailPutDTO();

        String jsonContent = createStoreDetailJson(createdStore.getStoreId(), storeDetailPutDTO.getAddress(), storeDetailPutDTO.getDescription());

        mockMvc.perform(put("/api/storesdetail/{id}", createdStoreDetail.getStoreDetailId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated store detail"));

        assertStoreDetailResponse(createdStoreDetail.getStoreDetailId(), "Updated Address", "Updated Description", createdStore.getStoreId());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testPartialUpdateStoreDetail() throws Exception {
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        StoreDetailCreateDTO storeDetailCreateDTO = createStoreDetailCreateDTO(createdStore);
        StoreDetail createdStoreDetail = storeDetailService.saveStoreDetail(storeDetailCreateDTO);

        StoreDetailPatchDTO storeDetailPatchDTO = createStoreDetailPatchDTO();

        String jsonContent = "{ \"address\": \"" + storeDetailPatchDTO.getAddress() + "\" }";

        mockMvc.perform(patch("/api/storesdetail/{id}", createdStoreDetail.getStoreDetailId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated store detail"));

        assertStoreDetailResponse(createdStoreDetail.getStoreDetailId(), "Partially Updated Address", createdStoreDetail.getDescription(), createdStore.getStoreId());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testDeleteStoreDetail() throws Exception {
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        StoreDetailCreateDTO storeDetailCreateDTO = createStoreDetailCreateDTO(createdStore);
        StoreDetail createdStoreDetail = storeDetailService.saveStoreDetail(storeDetailCreateDTO);

        mockMvc.perform(delete("/api/storesdetail/{id}", createdStoreDetail.getStoreDetailId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("Successfully deleted store detail"));

        assertFalse(storeDetailRepository.existsById(createdStoreDetail.getStoreDetailId()));
    }
}
