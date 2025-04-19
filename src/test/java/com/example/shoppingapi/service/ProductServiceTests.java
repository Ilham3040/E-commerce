package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.modelhelper.*;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private ProductService productService;

    private final ModelHelper<Product> helper =
    ModelHelperFactory.getModelHelper(Product.class);

    @Test
    void getAllProducts_returnsAllProducts() {
        List<Product> expected = List.of(
            helper.createModel(1),
            helper.createModel(2)
        );
        when(productRepository.findAll()).thenReturn(expected);

        List<Product> actual = productService.getAllProducts();
        assertEquals(expected, actual);
        verify(productRepository).findAll();
    }

    @Test
    void getProductById_found_returnsProduct() {
        Product expected = helper.createModel(1);
        when(productRepository.findById(1L))
            .thenReturn(Optional.of(expected));

        Product actual = productService.getProductById(1L);
        assertEquals(expected, actual);
        verify(productRepository).findById(1L);
    }

    @Test
    void getProductById_notFound_throwsException() {
        when(productRepository.findById(2L))
            .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> productService.getProductById(2L)
        );
        assertEquals("Product not found with ID: 2", ex.getMessage());
        verify(productRepository).findById(2L);
    }

    @Test
    void saveProduct_missingStore_throwsException() {
        Product toSave = Product.builder().productId(1L).build();

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> productService.saveProduct(toSave)
        );
        assertEquals(
            "Store ID is required to create a product.",
            ex.getMessage()
        );
        verify(productRepository, never()).save(any());
    }

    @Test
    void saveProduct_storeNotFound_throwsException() {
        Product product = helper.createModel(1);
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> productService.saveProduct(product)
        );
        assertEquals("Store not found with ID: 1", ex.getMessage());
        verify(storeRepository).findById(1L);
        verify(productRepository, never()).save(any());
    }

    @Test
    void saveProduct_success_savesAndReturnsProduct() {
        Product toSave = helper.createModel(1);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(toSave.getStore()));
        when(productRepository.save(any())).thenReturn(toSave);

        Product saved = productService.saveProduct(toSave);
        assertEquals(toSave, saved);
        verify(storeRepository).findById(1L);
        verify(productRepository).save(toSave);
    }

    @Test
    void updateProduct_idMismatch_throwsException() {

        Product mismatch = helper.createModel(2);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> productService.updateProduct(1L, mismatch)
        );
        assertEquals(
            "Product ID in URL and body must match.",
            ex.getMessage()
        );
        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProduct_notFound_throwsException() {

        Product toUpdate = helper.createModel(1);
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> productService.updateProduct(1L, toUpdate)
        );
        assertEquals("Product not found with ID: 1", ex.getMessage());
        verify(productRepository).findById(1L);
    }

    @Test
    void updateProduct_missingStore_throwsException() {

        Product original = helper.createModel(1);
        when(productRepository.findById(1L)).thenReturn(Optional.of(original));
        Product noStore = original.toBuilder().store(null).build();

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> productService.updateProduct(1L, noStore)
        );
        assertEquals(
            "Store ID is required to update a product.",
            ex.getMessage()
        );
        verify(productRepository).findById(1L);
        verify(storeRepository, never()).findById(any());
    }

    @Test
    void updateProduct_storeNotFound_throwsException() {

        Product original = helper.createModel(1);
        when(productRepository.findById(1L)).thenReturn(Optional.of(original));
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> productService.updateProduct(1L, original)
        );
        assertEquals("Store not found with ID: 1", ex.getMessage());
        verify(storeRepository).findById(1L);
        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProduct_success_savesUpdatedProduct() {

        Product original = helper.createModel(1);
        when(productRepository.findById(1L)).thenReturn(Optional.of(original));
        when(storeRepository.findById(1L)).thenReturn(Optional.of(original.getStore()));
        when(productRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Product updated = original.toBuilder().price(BigDecimal.valueOf(35000)).build();
        Product result = productService.updateProduct(1L, updated);

        assertEquals(updated, result);
        verify(productRepository).findById(1L);
        verify(storeRepository).findById(1L);
        verify(productRepository).save(updated);
    }

    @Test
    void partialUpdateProduct_existing_appliesUpdates() {

        Product original = helper.createModel(1);
        when(productRepository.findById(1L)).thenReturn(Optional.of(original));
        when(productRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Map<String, Object> changes = Map.of("price", 35000, "productName", "NewName");
        Product result = productService.partialUpdateProduct(1L, changes);

        assertEquals(BigDecimal.valueOf(35000), result.getPrice());
        assertEquals("NewName", result.getProductName());
        verify(productRepository).findById(1L);
        verify(productRepository).save(original);
    }

    @Test
    void deleteById_existing_invokesSoftDelete() {

        Product original = helper.createModel(1);

        when(productRepository.findById(1L))
            .thenReturn(Optional.of(original));
        doNothing().when(productRepository).delete(original);

        productService.deleteById(1L);

        verify(productRepository).findById(1L);
        verify(productRepository).delete(original);
    }

    @Test
    void deleteById_notFound_throwsException() {
        when(productRepository.findById(3L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> productService.deleteById(3L)
        );
        assertEquals("Product not found with ID: 3", ex.getMessage());
        verify(productRepository).findById(3L);
    }
}
