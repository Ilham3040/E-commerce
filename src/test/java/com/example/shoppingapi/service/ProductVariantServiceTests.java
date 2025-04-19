package com.example.shoppingapi.service;

import com.example.shoppingapi.model.ProductVariant;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.ProductVariantRepository;
import com.example.shoppingapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductVariantServiceTest {

    @Mock private ProductVariantRepository variantRepo;
    @Mock private ProductRepository        productRepo;
    @InjectMocks private ProductVariantService service;

    private final ModelHelper<ProductVariant> helper =
        ModelHelperFactory.getModelHelper(ProductVariant.class);

    @Test
    void findAll_returnsAllVariants() {
        List<ProductVariant> list = List.of(helper.createModel(1), helper.createModel(2));
        when(variantRepo.findAll()).thenReturn(list);

        assertEquals(list, service.findAll());
        verify(variantRepo).findAll();
    }

    @Test
    void findById_found_returnsVariant() {
        ProductVariant v = helper.createModel(1);
        when(variantRepo.findById(1L)).thenReturn(Optional.of(v));

        assertEquals(v, service.findById(1L));
    }

    @Test
    void findById_notFound_throwsException() {
        when(variantRepo.findById(2L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.findById(2L)
        );
        assertEquals("ProductVariant not found with ID: 2", ex.getMessage());
    }

    @Test
    void saveProductVariant_success_savesAndReturns() {
        ProductVariant v = helper.createModel(1);
        when(productRepo.existsById(v.getProduct().getProductId())).thenReturn(true);
        when(variantRepo.save(any())).thenReturn(v);

        assertEquals(v, service.saveProductVariant(v));
        verify(variantRepo).save(v);
    }

    @Test
    void saveProductVariant_missingProduct_throwsException() {
        ProductVariant v = helper.createModel(1);
        v.setProduct(null);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> service.saveProductVariant(v)
        );
        assertEquals("Product ID is required to create a product variant.", ex.getMessage());
    }

    @Test
    void saveProductVariant_productNotFound_throwsException() {
        ProductVariant v = helper.createModel(1);
        when(productRepo.existsById(v.getProduct().getProductId())).thenReturn(false);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> service.saveProductVariant(v)
        );
        assertEquals("Product not found. Cannot create product variant.", ex.getMessage());
    }

    @Test
    void updateProductVariant_success_savesUpdated() {
        ProductVariant orig = helper.createModel(1);
        when(variantRepo.findById(1L)).thenReturn(Optional.of(orig));
        when(productRepo.existsById(orig.getProduct().getProductId())).thenReturn(true);
        when(variantRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        ProductVariant upd = orig.toBuilder().variantName("New").build();
        ProductVariant res = service.updateProductVariant(1L, upd);

        assertEquals("New", res.getVariantName());
    }

    @Test
    void partialUpdateProductVariant_appliesUpdates() {
        ProductVariant v = helper.createModel(1);
        when(variantRepo.findById(1L)).thenReturn(Optional.of(v));
        when(variantRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Map<String,Object> changes = Map.of(
            "variantName","X", "stockQuantity",50
        );
        ProductVariant res = service.partialUpdateProductVariant(1L, changes);

        assertEquals("X", res.getVariantName());
        assertEquals(50,   res.getStockQuantity());
    }

    @Test
    void softDeleteProductVariant_setsDeletedAt() {
        ProductVariant v = helper.createModel(1);
        when(variantRepo.findById(1L)).thenReturn(Optional.of(v));
        when(variantRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        ProductVariant res = service.softDeleteProductVariant(1L);
        assertNotNull(res.getDeletedAt());
        assertTrue(res.getDeletedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
