package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    // Simple builder for Store
    private Store buildStore(long id) {
        return Store.builder()
                    .storeId(id)
                    .storeName("Store" + id)
                    .deletedAt(null)
                    .build();
    }

    // Simple builder for Product
    private Product buildProduct(long id, Store store) {
        return Product.builder()
                      .productId(id)
                      .productName("Product" + id)
                      .price(BigDecimal.valueOf(10000 + id))
                      .store(store)
                      .deletedAt(null)
                      .build();
    }

    @Test
    void getAllProducts_returnsAllProducts() {
        Store store = buildStore(1);
        List<Product> expected = List.of(
            buildProduct(1, store),
            buildProduct(2, store)
        );
        when(productRepository.findAll()).thenReturn(expected);

        List<Product> actual = productService.getAllProducts();
        assertEquals(expected, actual);
        verify(productRepository).findAll();
    }

    @Test
    void getProductById_found_returnsProduct() {
        Store store = buildStore(1);
        Product expected = buildProduct(1, store);
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
        Store store = buildStore(1);
        Product toSave = buildProduct(1, store);
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> productService.saveProduct(toSave)
        );
        assertEquals("Store not found with ID: 1", ex.getMessage());
        verify(storeRepository).findById(1L);
        verify(productRepository, never()).save(any());
    }

    @Test
    void saveProduct_success_savesAndReturnsProduct() {
        Store store = buildStore(1);
        Product toSave = buildProduct(1, store);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(productRepository.save(any())).thenReturn(toSave);

        Product saved = productService.saveProduct(toSave);
        assertEquals(toSave, saved);
        verify(storeRepository).findById(1L);
        verify(productRepository).save(toSave);
    }

    @Test
    void updateProduct_idMismatch_throwsException() {
        Store store = buildStore(1);
        Product mismatch = buildProduct(2, store);

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
        Store store = buildStore(1);
        Product toUpdate = buildProduct(1, store);
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
        Store store = buildStore(1);
        Product original = buildProduct(1, store);
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
        Store store = buildStore(1);
        Product original = buildProduct(1, store);
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
        Store store = buildStore(1);
        Product original = buildProduct(1, store);
        when(productRepository.findById(1L)).thenReturn(Optional.of(original));
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
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
        Store store = buildStore(1);
        Product original = buildProduct(1, store);
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
    void softDeleteProduct_existing_setsDeletedAt() {
        Store store = buildStore(1);
        Product original = buildProduct(1, store);
        when(productRepository.findById(1L)).thenReturn(Optional.of(original));
        when(productRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Product result = productService.softDeleteProduct(1L);
        assertNotNull(result.getDeletedAt());
        assertTrue(result.getDeletedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        verify(productRepository).findById(1L);
        verify(productRepository).save(original);
    }

    @Test
    void softDeleteProduct_notFound_throwsException() {
        when(productRepository.findById(3L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> productService.softDeleteProduct(3L)
        );
        assertEquals("Product not found with ID: 3", ex.getMessage());
        verify(productRepository).findById(3L);
    }
}
