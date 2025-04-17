package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.StoreCategoryItemId;
import com.example.shoppingapi.model.StoreCategoryItem;
import com.example.shoppingapi.model.StoreCategoryItemId;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.StoreRepository;
import com.example.shoppingapi.repository.StoreRoleRepository;
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

    private ModelHelper<StoreCategoryItem> storeCategoryItemHelper =
        ModelHelperFactory.getModelHelper(StoreCategoryItem.class);

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

        verify(storeCategoryRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        StoreCategoryItem storeCategoryItem = storeCategoryItemHelper.createModel(1);
        StoreCategoryItemId id = storeCategoryItem.getId();
        when(storeCategoryItemRepository.findById(storeCategoryItem.getId()))
            .thenReturn(Optional.of(storeCategoryItem));
        StoreCategoryItem result = storeCategoryItemService.findById(storeCategoryItem.getId()).orElseThrow(() -> new AssertionError("Cart Item doesn't exist"));
        assertNotNull(result);
        assertEquals(storeCategoryItem, result);

        verify(storeCategoryItemRepository, times(1)).findById(storeCategoryItem.getId());
    }

    @Test
    public void testFindById_NotFound() {
        StoreCategoryItemId missingId = StoreCategoryItemId.builder().categoryId(9L).productId(9L).build();

        when(storeCategoryItemRepository.findById(missingId)).thenReturn(Optional.empty());

        Optional<StoreCategoryItem> result = storeCategoryItemService.findById(missingId);

        assertFalse(result.isPresent());

        verify(storeCategoryItemRepository, times(1)).findById(missingId.getUserId());
    }

    @Test
    public void testSaveStoreCategoryItem() {
        StoreCategoryItem storeCategoryItem = storeCategoryItemHelper.createModel(1);

        when(store.findById(storeCategoryItem.getStoreCategory().getStoreId()))
            .thenReturn(Optional.of(storeCategoryItem.getStore()));
        when(storeCategoryItemRepository.findById(storeCategoryItem.getUser().getUserId()))
            .thenReturn(Optional.of(storeCategoryItem.getUser()));
        when(storeCategoryRepository.save(any(StoreCategoryItem.class))).thenReturn(storeCategoryItem);

        StoreCategoryItem created = storeCategoryItemService.saveStoreCategoryItem(storeCategoryItem);
        
        assertNotNull(created);
        assertEquals(storeCategoryItem, created);

        verify(storeCategoryRepository, times(1)).save(storeCategoryItem);
        verify(productRepository, times(1)).findById(storeCategoryItem.getStore().getStoreId());
        verify(storeCategoryItemRepository, times(1)).findById(storeCategoryItem.getUser().getUserId());
    }

    @Test
    public void testUpdateStoreCategoryItem() {
        StoreCategoryItem existing = storeCategoryItemHelper.createModel(1);
        StoreCategoryItemId id = existing.getId();

        when(storeCategoryRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(storeCategoryRepository.save(any(StoreCategoryItem.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
        when(storeCategoryItemRepository.findById(id.getUserId())).thenReturn(Optional.of(existing.getUser()));
        when(productRepository.findById(id.getStoreId())).thenReturn(Optional.of(existing.getStore()));
        
        StoreCategoryItem updated  = existing.toBuilder().role("admin").build();
        StoreCategoryItem result = storeCategoryItemService.updateStoreCategoryItem(existing.getId(), updated);

        assertNotNull(result);
        assertEquals("admin", result.getRole());

        verify(storeCategoryRepository, times(1)).findById(existing.getId());
        verify(storeCategoryRepository, times(1)).save(updated);
        verify(storeCategoryItemRepository, times(1)).findById(id.getUserId());
        verify(productRepository, times(1)).findById(id.getStoreId());
    }

    @Test
    public void testPartialUpdateStoreCategoryItem() {
        StoreCategoryItem storeCategoryItem = storeCategoryItemHelper.createModel(2);
        StoreCategoryItemId id = storeCategoryItem.getId();

        when(storeCategoryRepository.findById(id)).thenReturn(Optional.of(storeCategoryItem));
        when(storeCategoryRepository.save(any(StoreCategoryItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(storeCategoryItemRepository.findById(id.getUserId())).thenReturn(Optional.of(storeCategoryItem.getUser()));
        when(productRepository.findById(id.getStoreId())).thenReturn(Optional.of(storeCategoryItem.getStore()));

        Map<String, Object> updates = Map.of("role", "admin");
        StoreCategoryItem result = storeCategoryItemService.partialUpdateStoreCategoryItem(id, updates);

        assertNotNull(result);
        assertEquals("admin", result.getRole());

        verify(storeCategoryItemRepository, times(1)).findById(id.getUserId());
        verify(productRepository, times(1)).findById(id.getStoreId());
        verify(storeCategoryRepository, times(1)).findById(id);
        verify(storeCategoryRepository, times(1)).save(storeCategoryItem);
    }

        @Test
        public void testDeleteStoreCategoryItem() {
        StoreCategoryItem storeCategoryItem  = storeCategoryItemHelper.createModel(1);
        StoreCategoryItemId id = storeCategoryItem.getId();

        when(storeCategoryRepository.existsById(id))
            .thenReturn(true);
        doNothing().when(storeCategoryRepository).deleteById(id);
        when(storeCategoryItemRepository.findById(id.getUserId())).thenReturn(Optional.of(storeCategoryItem.getUser()));
        when(productRepository.findById(id.getStoreId())).thenReturn(Optional.of(storeCategoryItem.getStore()));

        storeCategoryItemService.deleteById(id);

        verify(storeCategoryItemRepository, times(1)).findById(id.getUserId());
        verify(productRepository, times(1)).findById(id.getStoreId());
        verify(storeCategoryRepository, times(1)).existsById(id);
        verify(storeCategoryRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteStoreCategoryItem_NotFound() {
        StoreCategoryItemId id = new StoreCategoryItemId(9L, 9L);
    
        
        when(productRepository.findById(id.getStoreId()))
            .thenReturn(Optional.of(Store.builder().storeId(9L).build()));
        when(storeCategoryItemRepository.findById(id.getUserId()))
            .thenReturn(Optional.of(User.builder().userId(9L).build()));
    
        
        when(storeCategoryRepository.existsById(id)).thenReturn(false);
    
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> storeCategoryItemService.deleteById(id)
        );
    
        assertEquals("Store Role not found with ID: " + id, ex.getMessage());
    
        
        verify(productRepository, times(1)).findById(id.getStoreId());
        verify(storeCategoryItemRepository, times(1)).findById(id.getUserId());
        verify(storeCategoryRepository, times(1)).existsById(id);
        verify(storeCategoryRepository, never()).deleteById(any());
    }
    
}
