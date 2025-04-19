package com.example.shoppingapi.service;

import com.example.shoppingapi.model.ProductReview;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.ProductReviewRepository;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductReviewServiceTest {

    @Mock private ProductReviewRepository reviewRepo;
    @Mock private ProductRepository       productRepo;
    @Mock private UserRepository          userRepo;
    @InjectMocks private ProductReviewService service;

    private final ModelHelper<ProductReview> helper =
        ModelHelperFactory.getModelHelper(ProductReview.class);

    @Test
    void findAll_returnsAllReviews() {
        List<ProductReview> list = List.of(helper.createModel(1), helper.createModel(2));
        when(reviewRepo.findAll()).thenReturn(list);

        assertEquals(list, service.findAll());
        verify(reviewRepo).findAll();
    }

    @Test
    void findById_found_returnsReview() {
        ProductReview pr = helper.createModel(1);
        when(reviewRepo.findById(1L)).thenReturn(Optional.of(pr));

        assertEquals(pr, service.findById(1L));
        verify(reviewRepo).findById(1L);
    }

    @Test
    void findById_notFound_throwsException() {
        when(reviewRepo.findById(2L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.findById(2L)
        );
        assertEquals("ProductReview not found with ID: 2", ex.getMessage());
    }

    @Test
    void saveProductReview_success_savesAndReturns() {
        ProductReview pr = helper.createModel(1);
        when(productRepo.existsById(pr.getProduct().getProductId())).thenReturn(true);
        when(userRepo.findById(pr.getUser().getUserId())).thenReturn(Optional.of(pr.getUser()));
        when(reviewRepo.save(any())).thenReturn(pr);

        assertEquals(pr, service.saveProductReview(pr));
        verify(reviewRepo).save(pr);
    }

    @Test
    void saveProductReview_missingProduct_throwsException() {
        ProductReview pr = helper.createModel(1);
        pr.setProduct(null);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> service.saveProductReview(pr)
        );
        assertEquals("Product ID is required to create a product review.", ex.getMessage());
        verify(reviewRepo, never()).save(any());
    }

    @Test
    void saveProductReview_productNotFound_throwsException() {
        ProductReview pr = helper.createModel(1);
        when(productRepo.existsById(pr.getProduct().getProductId())).thenReturn(false);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> service.saveProductReview(pr)
        );
        assertEquals("Product not found. Cannot create product review.", ex.getMessage());
    }

    @Test
    void updateProductReview_success_savesUpdated() {
        ProductReview orig = helper.createModel(1);
        when(reviewRepo.findById(1L)).thenReturn(Optional.of(orig));
        when(userRepo.findById(orig.getUser().getUserId())).thenReturn(Optional.of(orig.getUser()));
        when(productRepo.existsById(orig.getProduct().getProductId())).thenReturn(true);
        when(reviewRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        ProductReview upd = orig.toBuilder().description("Nice!").build();
        ProductReview res = service.updateProductReview(1L, upd);

        assertEquals("Nice!", res.getDescription());
        verify(reviewRepo).save(upd);
    }

    @Test
    void partialUpdateProductReview_appliesUpdates() {
        ProductReview pr = helper.createModel(1);
        when(reviewRepo.findById(1L)).thenReturn(Optional.of(pr));
        when(reviewRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Map<String,Object> changes = Map.of("description","Updated","starRating",4);
        ProductReview res = service.partialUpdateProductReview(1L, changes);

        assertEquals("Updated", res.getDescription());
        assertEquals(4, res.getStarRating());
    }

    @Test
    void deleteById_existing_invokesSoftDelete() {

        ProductReview original = helper.createModel(1);

        when(reviewRepo.findById(1L))
            .thenReturn(Optional.of(original));
        doNothing().when(reviewRepo).delete(original);

        service.deleteById(1L);

        verify(reviewRepo).findById(1L);
        verify(reviewRepo).delete(original);
    }
}
