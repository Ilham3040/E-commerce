package com.example.shoppingapi.service;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.example.shoppingapi.model.ProductReview;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.ProductReviewRepository;
import com.example.shoppingapi.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ProductReviewServiceTests.class)
public class ProductReviewServiceTests {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductReviewRepository productReviewRepository;

    @InjectMocks
    private ProductReviewService productReviewService;

    
    private ModelHelper<ProductReview> productReviewHelper = ModelHelperFactory.getModelHelper(ProductReview.class);


    @Test
    public void testGetAllProductReview(){
        ProductReview productReview1 = productReviewHelper.createModel(1);
        ProductReview productReview2 = productReviewHelper.createModel(2);

        List<ProductReview> mockProductReviews = Arrays.asList(productReview1,productReview2);

        when(productReviewRepository.findAll()).thenReturn(mockProductReviews);

        List<ProductReview> result = productReviewService.findAll();

        assertNotNull(result);
        assertEquals(mockProductReviews, result);

        verify(productReviewRepository,times(1)).findAll();
        
    }

    @Test
    public void testGetProductReviewById(){
        ProductReview productReview = productReviewHelper.createModel(1);

        ProductReview mockProductReview = productReview;

        when(productReviewRepository.findById(productReview.getReviewId())).thenReturn(Optional.of(productReview));

        ProductReview result = productReviewService.findById(productReview.getReviewId()).orElseThrow(() -> new AssertionError("Store not found"));

        assertNotNull(result);
        assertEquals(mockProductReview, result);

        verify(productReviewRepository,times(1)).findById(productReview.getReviewId());
        
    }

    @Test
    public void testGetProductReviewByIdNULL(){
        when(productReviewRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ProductReview> result = productReviewService.findById(1L);
        
        assertFalse(result.isPresent());
        verify(productReviewRepository, times(1)).findById(1L);
    }


    @Test
    public void testSaveProductReview() {
        ProductReview productReview = productReviewHelper.createModel(1);

        when(productReviewRepository.save(any(ProductReview.class))).thenReturn(productReview);
        when(userRepository.findById(productReview.getUser().getUserId())).thenReturn(Optional.of(productReview.getUser()));
        when(productRepository.findById(productReview.getProduct().getProductId())).thenReturn(Optional.of(productReview.getProduct()));

        ProductReview savedProductReview = productReviewService.saveProductReview(productReview);
        
        assertNotNull(savedProductReview);
        assertEquals(productReview, savedProductReview);

        verify(productReviewRepository,times(1)).save(productReview);
        verify(productRepository,times(1)).findById(productReview.getProduct().getProductId());
        verify(userRepository,times(1)).findById(productReview.getUser().getUserId());
    }

    @Test
    public void testUpdateProductReview() {
        ProductReview existing = productReviewHelper.createModel(1);
        
        when(productReviewRepository.findById(existing.getReviewId())).thenReturn(Optional.of(existing));
        when(userRepository.findById(existing.getUser().getUserId())).thenReturn(Optional.of(existing.getUser()));
        when(productRepository.findById(existing.getProduct().getProductId())).thenReturn(Optional.of(existing.getProduct()));
        when(productReviewRepository.save(any(ProductReview.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductReview updated = existing.toBuilder().description("Pretty good").build();
        ProductReview result = productReviewService.updateProductReview(existing.getReviewId(), updated);

        assertNotNull(result);
        assertEquals(updated, result);
        assertEquals("Pretty good", result.getDescription());

        verify(productReviewRepository, times(1)).findById(existing.getReviewId());
        verify(userRepository, times(1)).findById(existing.getUser().getUserId());
        verify(productRepository, times(1)).findById(existing.getProduct().getProductId());
        verify(productReviewRepository, times(1)).save(updated);
    }
    
    @Test
    public void testPartialUpdateProductReview() {
        ProductReview existing = productReviewHelper.createModel(1);
        
        when(productReviewRepository.findById(existing.getReviewId())).thenReturn(Optional.of(existing));
        when(productReviewRepository.save(any(ProductReview.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, Object> updates = Map.of("description", "Updated description");
        ProductReview result = productReviewService.partialUpdateProductReview(existing.getReviewId(), updates);

        assertNotNull(result);
        assertEquals("Updated description", result.getDescription());

        verify(productReviewRepository, times(1)).findById(existing.getReviewId());
        verify(productReviewRepository, times(1)).save(existing);
    }
    
    
    @Test
    public void testSoftdeleteById() {
        ProductReview productReview = productReviewHelper.createModel(1);
        
        when(productReviewRepository.findById(productReview.getReviewId())).thenReturn(Optional.of(productReview));
        when(productReviewRepository.save(any(ProductReview.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductReview deletedProductReview = productReviewService.deleteById(productReview.getReviewId());

        assertNotNull(deletedProductReview);
        assertNotNull(deletedProductReview.getDeletedAt());
        assertEquals(productReview.getReviewId(), deletedProductReview.getReviewId());

        verify(productReviewRepository, times(1)).save(deletedProductReview);
        verify(productReviewRepository, times(1)).findById(productReview.getReviewId());
    }

    @Test
    public void testSoftdeleteById_NotFound() {
        when(productReviewRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            productReviewService.deleteById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals("ProductReview not found with ID: 1", e.getMessage());
        }

        verify(productReviewRepository, times(1)).findById(1L);
    }




    
}
