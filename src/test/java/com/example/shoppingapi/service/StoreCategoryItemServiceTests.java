package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.StoreCategoryItemCreateDTO;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.StoreCategory;
import com.example.shoppingapi.model.StoreCategoryItem;
import com.example.shoppingapi.model.StoreCategoryItemId;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.StoreCategoryItemRepository;
import com.example.shoppingapi.repository.StoreCategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreCategoryItemServiceTest {

    @Mock
    private StoreCategoryItemRepository itemRepository;
    @Mock
    private StoreCategoryRepository    categoryRepository;
    @Mock
    private ProductRepository          productRepository;
    @InjectMocks
    private StoreCategoryItemService   service;

    @Test
    void getAllItems_returnsFullList() {
        List<StoreCategoryItem> allItems = Arrays.asList(
                new StoreCategoryItem(), new StoreCategoryItem()
        );
        when(itemRepository.findAll()).thenReturn(allItems);

        List<StoreCategoryItem> result = service.getAllItems();

        assertEquals(allItems, result);
        verify(itemRepository).findAll();
    }

    @Test
    void getStoreCategoryItemByID_itemFound_returnsItem() {
        StoreCategoryItemId itemId = new StoreCategoryItemId(1L, 2L);
        StoreCategoryItem existingItem = new StoreCategoryItem();
        existingItem.setId(itemId);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));

        StoreCategoryItem result = service.getStoreCategoryItemByID(itemId);

        assertSame(existingItem, result);
        verify(itemRepository).findById(itemId);
    }

    @Test
    void getStoreCategoryItemByID_notFound_throwsResourceNotFound() {
        StoreCategoryItemId missingId = new StoreCategoryItemId(9L, 9L);
        when(itemRepository.findById(missingId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getStoreCategoryItemByID(missingId)
        );
        assertEquals("Category Item not found with ID: " + missingId, ex.getMessage());
    }

    @Test
    void saveStoreCategoryItem_validData_savesAndReturnsNewItem() {
        // prepare DTO
        StoreCategoryItemCreateDTO createDTO = new StoreCategoryItemCreateDTO();
        createDTO.setCategoryId(1L);
        createDTO.setProductId(2L);

        // stub existence checks
        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(StoreCategory.builder().categoryId(1L).build()));
        when(productRepository.findById(2L))
                .thenReturn(Optional.of(Product.builder().productId(2L).build()));
        // echo back what service built
        when(itemRepository.save(any(StoreCategoryItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        StoreCategoryItem created = service.saveStoreCategoryItem(createDTO);

        assertNotNull(created);
        assertEquals(1L, created.getStoreCategory().getCategoryId());
        assertEquals(2L, created.getProduct().getProductId());
        verify(categoryRepository).findById(1L);
        verify(productRepository).findById(2L);
        verify(itemRepository).save(created);
    }

    @Test
    void saveStoreCategoryItem_categoryNotFound_throwsResourceNotFound() {
        StoreCategoryItemCreateDTO createDTO = new StoreCategoryItemCreateDTO();
        createDTO.setCategoryId(1L);
        createDTO.setProductId(2L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> service.saveStoreCategoryItem(createDTO)
        );
        assertEquals("Category not found with ID: 1", ex.getMessage());
        verify(categoryRepository).findById(1L);
        verifyNoInteractions(productRepository, itemRepository);
    }

    @Test
    void saveStoreCategoryItem_productNotFound_throwsResourceNotFound() {
        StoreCategoryItemCreateDTO createDTO = new StoreCategoryItemCreateDTO();
        createDTO.setCategoryId(1L);
        createDTO.setProductId(2L);

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(new StoreCategory()));
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> service.saveStoreCategoryItem(createDTO)
        );
        assertEquals("Product not found with ID: 2", ex.getMessage());
        verify(categoryRepository).findById(1L);
        verify(productRepository).findById(2L);
        verifyNoInteractions(itemRepository);
    }

    @Test
    void deleteItemFromCategory_existingItem_deletesSuccessfully() {
        StoreCategoryItemId itemId = new StoreCategoryItemId(3L, 4L);
        StoreCategoryItem toDelete = new StoreCategoryItem();
        toDelete.setId(itemId);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(toDelete));
        doNothing().when(itemRepository).delete(toDelete);

        service.deleteItemFromCategory(itemId);

        verify(itemRepository).findById(itemId);
        verify(itemRepository).delete(toDelete);
    }
}
