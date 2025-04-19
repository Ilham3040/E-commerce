package com.example.shoppingapi.service;

import com.example.shoppingapi.model.StoreCategoryItem;
import com.example.shoppingapi.model.StoreCategoryItemId;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.StoreCategoryItemRepository;
import com.example.shoppingapi.repository.StoreCategoryRepository;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.*;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = StoreCategoryItemServiceTests.class)
public class StoreCategoryItemServiceTests {

    @Mock
    private StoreCategoryItemRepository storeCategoryItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StoreCategoryRepository storeCategoryRepository;

    @InjectMocks
    private StoreCategoryItemService storeCategoryItemService;

    private final ModelHelper<StoreCategoryItem> storeCategoryItemHelper = ModelHelperFactory.getModelHelper(StoreCategoryItem.class);

    @Test
    public void testFindAll() {
        StoreCategoryItem storeCategoryItem1 = storeCategoryItemHelper.createModel(1);
        StoreCategoryItem storeCategoryItem2 = storeCategoryItemHelper.createModel(2);

        List<StoreCategoryItem> mockList = Arrays.asList(storeCategoryItem1, storeCategoryItem2);

        when(storeCategoryItemRepository.findAll()).thenReturn(mockList);

        List<StoreCategoryItem> result = storeCategoryItemService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(storeCategoryItem1, result.get(0));
        assertEquals(storeCategoryItem2, result.get(1));

        verify(storeCategoryItemRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        StoreCategoryItem storeCategoryItem = storeCategoryItemHelper.createModel(1);
        StoreCategoryItemId id = storeCategoryItem.getId();
        when(storeCategoryItemRepository.findById(id))
            .thenReturn(Optional.of(storeCategoryItem));
        StoreCategoryItem result = storeCategoryItemService.findById(id).orElseThrow(() -> new AssertionError("Item on this category doesn't exist"));
        assertNotNull(result);
        assertEquals(storeCategoryItem, result);

        verify(storeCategoryItemRepository, times(1)).findById(id);
    }

    @Test
    public void testFindById_NotFound() {
        StoreCategoryItemId missingId = StoreCategoryItemId.builder().categoryId(9L).productId(9L).build();

        when(storeCategoryItemRepository.findById(missingId)).thenReturn(Optional.empty());

        Optional<StoreCategoryItem> result = storeCategoryItemService.findById(missingId);

        assertFalse(result.isPresent());

        verify(storeCategoryItemRepository, times(1)).findById(missingId);
    }

    @Test
    public void testSaveStoreCategoryItem() {
        StoreCategoryItem storeCategoryItem = storeCategoryItemHelper.createModel(1);

        when(storeCategoryRepository.findById(storeCategoryItem.getStoreCategory().getCategoryId()))
            .thenReturn(Optional.of(storeCategoryItem.getStoreCategory()));
        when(productRepository.findById(storeCategoryItem.getProduct().getProductId()))
            .thenReturn(Optional.of(storeCategoryItem.getProduct()));
        when(storeCategoryItemRepository.save(any(StoreCategoryItem.class))).thenReturn(storeCategoryItem);

        StoreCategoryItem created = storeCategoryItemService.saveStoreCategoryItem(storeCategoryItem);
        
        assertNotNull(created);
        assertEquals(storeCategoryItem, created);

        verify(storeCategoryItemRepository, times(1)).save(storeCategoryItem);
        verify(productRepository, times(1)).findById(storeCategoryItem.getProduct().getProductId());
        verify(storeCategoryRepository, times(1)).findById(storeCategoryItem.getStoreCategory().getCategoryId());
    }

    @Test
    public void testUpdateStoreCategoryItem() {
        StoreCategoryItem storeCategoryItem = storeCategoryItemHelper.createModel(1);
        StoreCategoryItemId id = storeCategoryItem.getId();

        when(storeCategoryRepository.findById(storeCategoryItem.getStoreCategory().getCategoryId()))
            .thenReturn(Optional.of(storeCategoryItem.getStoreCategory()));
        when(productRepository.findById(storeCategoryItem.getProduct().getProductId()))
            .thenReturn(Optional.of(storeCategoryItem.getProduct()));
        when(storeCategoryItemRepository.save(any(StoreCategoryItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(storeCategoryItemRepository.findById(id)).thenReturn(Optional.of(storeCategoryItem));


        StoreCategoryItem updated  = storeCategoryItem.toBuilder().updatedAt(LocalDateTime.now()).build();
        StoreCategoryItem result = storeCategoryItemService.updateStoreCategoryItem(storeCategoryItem.getId(), updated);

        assertNotNull(result);
        assertEquals(updated, result);
        
        verify(storeCategoryItemRepository, times(1)).save(updated);
        verify(productRepository, times(1)).findById(storeCategoryItem.getProduct().getProductId());
        verify(storeCategoryRepository, times(1)).findById(storeCategoryItem.getStoreCategory().getCategoryId());
        verify(storeCategoryItemRepository, times(1)).findById(id);
    }

    @Test
    public void testPartialUpdateStoreCategoryItem() {
        StoreCategoryItem storeCategoryItem = storeCategoryItemHelper.createModel(2);
        StoreCategoryItemId id = storeCategoryItem.getId();

        when(storeCategoryItemRepository.save(any(StoreCategoryItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(storeCategoryItemRepository.findById(id)).thenReturn(Optional.of(storeCategoryItem));

        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> updates = Map.of("updatedAt", now);
        StoreCategoryItem result = storeCategoryItemService.partialUpdateStoreCategoryItem(id, updates);

        assertNotNull(result);
        assertEquals(now, result.getUpdatedAt());

        verify(storeCategoryItemRepository, times(1)).save(storeCategoryItem);
        verify(storeCategoryItemRepository, times(1)).findById(id);
    }

        @Test
        public void testDeleteStoreCategoryItem() {
        StoreCategoryItem storeCategoryItem  = storeCategoryItemHelper.createModel(1);
        StoreCategoryItemId id = storeCategoryItem.getId();

        when(storeCategoryItemRepository.findById(id)).thenReturn(Optional.of(storeCategoryItem));
        when(storeCategoryItemRepository.save(any(StoreCategoryItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        storeCategoryItemService.deleteById(id);

        verify(storeCategoryItemRepository, times(1)).findById(id);
        verify(storeCategoryItemRepository, times(1)).save(storeCategoryItem);
    }

    @Test
    public void testDeleteStoreCategoryItem_NotFound() {
        StoreCategoryItemId id = new StoreCategoryItemId(9L, 9L);
            when(storeCategoryItemRepository.findById(id)).thenReturn(Optional.empty());
    
        try {
            storeCategoryItemService.deleteById(id);
        } catch (ResourceNotFoundException e) {
            assertEquals(("Item on this category not found with ID: " + id), e.getMessage());
        }
    
        verify(storeCategoryItemRepository, times(1)).findById(id);
    }
    
}
