package com.example.shoppingapi.controller;

import com.example.shoppingapi.EntityCreationHelper;
import com.example.shoppingapi.model.*;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback(true)
public class ProductReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityCreationHelper entityCreationHelper;

    @Test
    public void testGetAllProductReviews() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        ProductReview createdProductReview = entityCreationHelper.createProductReview(createdUser, createdProduct);

        mockMvc.perform(get("/api/productsreviews/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched all product reviews"))
                .andExpect(jsonPath("$.data[0].productId").value(createdProduct.getProductId()));
    }

    @Test
    public void testGetProductReviewById() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        ProductReview createdProductReview = entityCreationHelper.createProductReview(createdUser, createdProduct);

        mockMvc.perform(get("/api/productsreviews/{id}", createdProductReview.getReviewId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched product review"))
                .andExpect(jsonPath("$.data.productId").value(createdProductReview.getProduct().getProductId()))
                .andExpect(jsonPath("$.data.userId").value(createdProductReview.getUser().getUserId()))
                .andExpect(jsonPath("$.data.starRating").value(createdProductReview.getStarRating()))
                .andExpect(jsonPath("$.data.description").value(createdProductReview.getDescription()));
    }

    @Test
    public void testCreateProductReview() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);

        String productReviewJson = "{\n" +
                "  \"productId\": " + createdProduct.getProductId() + ",\n" +
                "  \"userId\": " + createdUser.getUserId() + ",\n" +
                "  \"starRating\": 5,\n" +
                "  \"description\": \"Great product!\"\n" +
                "}";

        MvcResult postResult = mockMvc.perform(post("/api/productsreviews/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productReviewJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created product review"))
                .andReturn();

        String responseContent = postResult.getResponse().getContentAsString();
        String reviewId = JsonPath.read(responseContent, "$.data.productReviewId").toString();


        mockMvc.perform(get("/api/productsreviews/{id}", reviewId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched product review"))
                .andExpect(jsonPath("$.data.productId").value(createdProduct.getProductId()))
                .andExpect(jsonPath("$.data.userId").value(createdUser.getUserId()))
                .andExpect(jsonPath("$.data.starRating").value(5))
                .andExpect(jsonPath("$.data.description").value("Great product!"));
    }

    @Test
    public void testUpdateProductReview() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        ProductReview createdProductReview = entityCreationHelper.createProductReview(createdUser, createdProduct);

        String productReviewJson = "{\n" +
                "  \"starRating\": 4,\n" +
                "  \"description\": \"Updated review!\"\n" +
                "}";

        mockMvc.perform(put("/api/productsreviews/{id}", createdProductReview.getReviewId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productReviewJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated product review"));

        mockMvc.perform(get("/api/productsreviews/{id}", createdProductReview.getReviewId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched product review"))
                .andExpect(jsonPath("$.data.productId").value(createdProductReview.getProduct().getProductId()))
                .andExpect(jsonPath("$.data.userId").value(createdProductReview.getUser().getUserId()))
                .andExpect(jsonPath("$.data.starRating").value(4))
                .andExpect(jsonPath("$.data.description").value("Updated review!"));
    }

    @Test
    public void testDeleteProductReview() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        ProductReview createdProductReview = entityCreationHelper.createProductReview(createdUser, createdProduct);

        mockMvc.perform(delete("/api/productsreviews/{id}", createdProductReview.getReviewId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("Successfully deleted product review"));
    }
}