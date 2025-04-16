package com.example.shoppingapi.service;

import com.example.shoppingapi.model.ProductVariant;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.ProductVariantRepository;
import com.example.shoppingapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ProductVariantServiceTests.class)
public class ProductVariantServiceTests {

    @Mock
    private ProductVariantRepository productVariantRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductVariantService productVariantService;

    // Helper for creating sample ProductVariant objects.
    private ModelHelper<ProductVariant> productVariantHelper = ModelHelperFactory.getModelHelper(ProductVariant.class);

    @Test
    public void testFindAll() {
        ProductVariant pv1 = productVariantHelper.createModel(1);
        ProductVariant pv2 = productVariantHelper.createModel(2);
        List<ProductVariant> variantList = Arrays.asList(pv1, pv2);

        when(productVariantRepository.findAll()).thenReturn(variantList);

        List<ProductVariant> result = productVariantService.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productVariantRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_Found() {
        ProductVariant pv = productVariantHelper.createModel(1);
        when(productVariantRepository.findById(pv.getVariantId())).thenReturn(Optional.of(pv));

        Optional<ProductVariant> result = productVariantService.findById(pv.getVariantId());
        assertTrue(result.isPresent());
        assertEquals(pv.getVariantId(), result.get().getVariantId());
        verify(productVariantRepository, times(1)).findById(pv.getVariantId());
    }

    @Test
    public void testFindById_NotFound() {
        when(productVariantRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<ProductVariant> result = productVariantService.findById(1L);
        assertFalse(result.isPresent());
        verify(productVariantRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindByProductId() {
        ProductVariant pv = productVariantHelper.createModel(1);
        Long productId = pv.getProduct().getProductId();
        when(productVariantRepository.findProductVariantbyProductId(productId)).thenReturn(Optional.of(pv));

        Optional<ProductVariant> result = productVariantService.findByProductId(productId);
        assertTrue(result.isPresent());
        assertEquals(productId, result.get().getProduct().getProductId());
        verify(productVariantRepository, times(1)).findProductVariantbyProductId(productId);
    }

    @Test
    public void testSaveProductVariant_Success() {
        ProductVariant pv = productVariantHelper.createModel(1);
        Product product = pv.getProduct();

        when(productRepository.existsById(product.getProductId())).thenReturn(true);
        when(productVariantRepository.save(any(ProductVariant.class))).thenReturn(pv);

        ProductVariant created = productVariantService.saveProductVariant(pv);
        assertNotNull(created);
        assertEquals(pv.getVariantId(), created.getVariantId());
        verify(productRepository, times(1)).existsById(product.getProductId());
        verify(productVariantRepository, times(1)).save(pv);
    }

    @Test
    public void testSaveProductVariant_ProductNotFound_ThrowsException() {
        ProductVariant pv = productVariantHelper.createModel(1);
        Product product = pv.getProduct();

        when(productRepository.existsById(product.getProductId())).thenReturn(false);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> productVariantService.saveProductVariant(pv));
        assertEquals("Product not found. Cannot create product variant.", exception.getMessage());
        verify(productRepository, times(1)).existsById(product.getProductId());
        verify(productVariantRepository, never()).save(any(ProductVariant.class));
    }

    @Test
    public void testUpdateProductVariant_Success() {
        ProductVariant existing = productVariantHelper.createModel(1);
        ProductVariant updated = productVariantHelper.createModel(2);
        updated.setVariantId(existing.getVariantId());

        when(productVariantRepository.findById(existing.getVariantId())).thenReturn(Optional.of(existing));

        when(productVariantRepository.findById(updated.getProduct().getProductId()))
                .thenReturn(Optional.of(existing));
        when(productVariantRepository.save(any(ProductVariant.class))).thenReturn(updated);

        ProductVariant result = productVariantService.updateProductVariant(existing.getVariantId(), updated);
        assertNotNull(result);
        assertEquals(updated.getVariantId(), result.getVariantId());
        assertEquals(updated.getVariantName(), result.getVariantName());
        verify(productVariantRepository, times(1)).findById(existing.getVariantId());
        verify(productVariantRepository, times(1)).save(updated);
    }

    @Test
    public void testPartialUpdateProductVariant_Success() {
        ProductVariant existing = productVariantHelper.createModel(1);
        when(productVariantRepository.findById(existing.getVariantId())).thenReturn(Optional.of(existing));
        when(productVariantRepository.save(any(ProductVariant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, Object> updates = Map.of(
            "variantName", "Updated Variant Name",
            "productReviews", 150,
            "stockQuantity", 25,
            "totalSold", 100
        );

        ProductVariant result = productVariantService.partialUpdateProductVariant(existing.getVariantId(), updates);
        assertNotNull(result);
        assertEquals("Updated Variant Name", result.getVariantName());
        assertEquals(150, result.getProductReviews());
        assertEquals(25, result.getStockQuantity());
        assertEquals(100, result.getTotalSold());
        verify(productVariantRepository, times(1)).findById(existing.getVariantId());
        verify(productVariantRepository, times(1)).save(existing);
    }

    @Test
    public void testSoftdeleteById() {
        ProductVariant productVariant = productVariantHelper.createModel(1);
        when(productVariantRepository.findById(productVariant.getVariantId())).thenReturn(Optional.of(productVariant));
        when(productVariantRepository.save(any(ProductVariant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductVariant deletedVariant = productVariantService.deleteById(productVariant.getVariantId());

        assertNotNull(deletedVariant);
        assertNotNull(deletedVariant.getDeletedAt());
        assertEquals(deletedVariant.getVariantId(),productVariant.getVariantId());

        verify(productVariantRepository, times(1)).save(productVariant);
        verify(productVariantRepository, times(1)).findById(productVariant.getVariantId());
    }

    @Test
    public void testSoftdeleteById_NotFound() {
        when(productVariantRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            productVariantService.deleteById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals("ProductVariant not found with ID: 1", e.getMessage());
        }

        verify(productVariantRepository, times(1)).findById(1L);
    }

}
