package com.example.shoppingapi.controller;

import com.example.shoppingapi.DotenvLoader;
import com.example.shoppingapi.dto.create.UserCreateDTO;
import com.example.shoppingapi.dto.create.StoreCreateDTO;
import com.example.shoppingapi.dto.create.ProductCreateDTO;
import com.example.shoppingapi.dto.create.ProductDetailCreateDTO;
import com.example.shoppingapi.dto.put.ProductDetailPutDTO;
import com.example.shoppingapi.dto.patch.ProductDetailPatchDTO;
import com.example.shoppingapi.model.ProductDetail;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.ProductDetailRepository;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.StoreRepository;
import com.example.shoppingapi.repository.UserRepository;
import com.example.shoppingapi.service.ProductDetailService;
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
public class ProductDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private ProductDetailRepository productDetailRepository;

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

    @BeforeAll
    public static void setUp() {
        DotenvLoader.load();
    }

    // Create a new User
    private User createUser() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername("testuser");
        userCreateDTO.setEmail("testuser" + System.currentTimeMillis() + "@example.com");
        userCreateDTO.setPhoneNumber("1234567890");
        return userService.createUser(userCreateDTO);
    }

    // Create a Store and link it with User
    private Store createStore(User user) throws Exception {
        StoreCreateDTO storeCreateDTO = new StoreCreateDTO();
        storeCreateDTO.setStoreName("Test Store");
        storeCreateDTO.setUserId(user.getUserId());
        return storeService.saveStore(storeCreateDTO);
    }

    // Create Product linked to the store
    private Product createProduct(Store store) throws Exception {
        ProductCreateDTO productCreateDTO = new ProductCreateDTO();
        productCreateDTO.setProductName("Test Product");
        productCreateDTO.setPrice(new BigDecimal("9.99"));
        productCreateDTO.setStoreId(store.getStoreId());
        return productService.saveProduct(productCreateDTO);
    }

    private ProductDetailCreateDTO createProductDetailCreateDTO(Product product) {
        ProductDetailCreateDTO productDetailCreateDTO = new ProductDetailCreateDTO();
        productDetailCreateDTO.setProductId(product.getProductId());
        productDetailCreateDTO.setDescription("Product description");
        return productDetailCreateDTO;
    }

    private ProductDetailPutDTO createProductDetailPutDTO() {
        ProductDetailPutDTO productDetailPutDTO = new ProductDetailPutDTO();
        productDetailPutDTO.setDescription("Updated description");
        return productDetailPutDTO;
    }

    private ProductDetailPatchDTO createProductDetailPatchDTO() {
        ProductDetailPatchDTO productDetailPatchDTO = new ProductDetailPatchDTO();
        productDetailPatchDTO.setDescription("Partially Updated description");
        return productDetailPatchDTO;
    }

    private String createProductDetailJson(Long productId, String description) {
        return String.format("{ \"productId\": %d, \"description\": \"%s\" }", productId, description);
    }

    private void assertProductDetailResponse(Long productDetailId, String description, Long productId) throws Exception {
        mockMvc.perform(get("/api/productsdetail/{id}", productDetailId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Fetched product detail"))
                .andExpect(jsonPath("$.data.productDetailId").value(productDetailId))
                .andExpect(jsonPath("$.data.description").value(description))
                .andExpect(jsonPath("$.data.productId").value(productId));
    }

    @Test
    public void testCreateProductDetail() throws Exception {
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        Product createdProduct = createProduct(createdStore);

        // Create ProductDetailCreateDTO with the newly created product
        ProductDetailCreateDTO productDetailCreateDTO = createProductDetailCreateDTO(createdProduct);

        String jsonContent = createProductDetailJson(productDetailCreateDTO.getProductId(), productDetailCreateDTO.getDescription());

        // Perform the POST request to create the product detail
        String responseContent = mockMvc.perform(post("/api/productsdetail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created product detail"))
                .andReturn().getResponse().getContentAsString();

        Integer createdProductDetailIdInteger = JsonPath.parse(responseContent).read("$.data.productDetailId");

        Long createdProductDetailId = Long.valueOf(createdProductDetailIdInteger.toString());

        ProductDetail createdProductDetail = productDetailService.getProductDetailByProductId(createdProduct.getProductId());
        assertEquals(productDetailCreateDTO.getDescription(), createdProductDetail.getDescription());
        assertEquals(productDetailCreateDTO.getProductId(), createdProductDetail.getProduct().getProductId());
    }

    @Test
    public void testGetAllProductDetails() throws Exception {
        // Create user, store, product, and product detail
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        Product createdProduct = createProduct(createdStore);
        ProductDetailCreateDTO productDetailCreateDTO = createProductDetailCreateDTO(createdProduct);
        ProductDetail savedProductDetail = productDetailService.saveProductDetail(productDetailCreateDTO);

        mockMvc.perform(get("/api/productsdetail"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched all product details"))
                .andExpect(jsonPath("$.data[0].productDetailId").value(savedProductDetail.getProductDetailId()));
    }

    @Test
    public void testGetProductDetailById() throws Exception {
        // Create user, store, product, and product detail
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        Product createdProduct = createProduct(createdStore);
        ProductDetailCreateDTO productDetailCreateDTO = createProductDetailCreateDTO(createdProduct);
        ProductDetail createdProductDetail = productDetailService.saveProductDetail(productDetailCreateDTO);

        mockMvc.perform(get("/api/productsdetail/{id}", createdProduct.getProductId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched product detail"))
                .andExpect(jsonPath("$.data.productDetailId").value(createdProductDetail.getProductDetailId()))
                .andExpect(jsonPath("$.data.description").value(createdProductDetail.getDescription()))
                .andExpect(jsonPath("$.data.product.productId").value(createdProduct.getProductId()));
    }

    @Test
    public void testUpdateProductDetail() throws Exception {
        // Create product detail for testing
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        Product createdProduct = createProduct(createdStore);
        ProductDetailCreateDTO productDetailCreateDTO = createProductDetailCreateDTO(createdProduct);
        ProductDetail createdProductDetail = productDetailService.saveProductDetail(productDetailCreateDTO);

        ProductDetailPutDTO productDetailPutDTO = createProductDetailPutDTO();

        String jsonContent = createProductDetailJson(createdProductDetail.getProduct().getProductId(), "updated");

        mockMvc.perform(put("/api/productsdetail/{id}", createdProduct.getProductId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated product detail"));

        mockMvc.perform(get("/api/productsdetail/{id}", createdProduct.getProductId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched product detail"))
                .andExpect(jsonPath("$.data.productDetailId").value(createdProductDetail.getProductDetailId()))
                .andExpect(jsonPath("$.data.description").value("updated"))
                .andExpect(jsonPath("$.data.product.productId").value(createdProduct.getProductId()));
    }

    @Test
    public void testPartialUpdateProductDetail() throws Exception {
        // Create product detail for testing
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        Product createdProduct = createProduct(createdStore);
        ProductDetailCreateDTO productDetailCreateDTO = createProductDetailCreateDTO(createdProduct);
        ProductDetail createdProductDetail = productDetailService.saveProductDetail(productDetailCreateDTO);

        // Use helper method to create ProductDetailPatchDTO
        ProductDetailPatchDTO productDetailPatchDTO = createProductDetailPatchDTO();

        String jsonContent = "{ \"description\": \"updated\" }";

        mockMvc.perform(patch("/api/productsdetail/{id}", createdProduct.getProductId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully partially updated product detail"));

        mockMvc.perform(get("/api/productsdetail/{id}", createdProduct.getProductId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched product detail"))
                .andExpect(jsonPath("$.data.productDetailId").value(createdProductDetail.getProductDetailId()))
                .andExpect(jsonPath("$.data.description").value("updated"))
                .andExpect(jsonPath("$.data.product.productId").value(createdProduct.getProductId()));
    }

    @Test
    public void testDeleteProductDetail() throws Exception {
        // Create product detail for testing
        User createdUser = createUser();
        Store createdStore = createStore(createdUser);
        Product createdProduct = createProduct(createdStore);
        ProductDetailCreateDTO productDetailCreateDTO = createProductDetailCreateDTO(createdProduct);
        ProductDetail createdProductDetail = productDetailService.saveProductDetail(productDetailCreateDTO);

        mockMvc.perform(delete("/api/productsdetail/{id}", createdProduct.getProductId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("Successfully deleted product detail"));

        assertFalse(productDetailRepository.existsById(createdProductDetail.getProductDetailId()));
    }
}
