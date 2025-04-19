package com.example.shoppingapi.service;

import com.example.shoppingapi.model.StoreCategoryItem;
import com.example.shoppingapi.model.StoreCategoryItemId;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.StoreCategoryItemRepository;
import com.example.shoppingapi.repository.StoreCategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreCategoryItemServiceTest {

    @Mock private StoreCategoryItemRepository itemRepo;
    @Mock private StoreCategoryRepository     categoryRepo;
    @Mock private ProductRepository           productRepo;
    @InjectMocks private StoreCategoryItemService service;

    private final ModelHelper<StoreCategoryItem> helper =
        ModelHelperFactory.getModelHelper(StoreCategoryItem.class);

    @Test
    void findAll_returnsAll() {
        List<StoreCategoryItem> list = Arrays.asList(helper.createModel(1), helper.createModel(2));
        when(itemRepo.findAll()).thenReturn(list);

        assertEquals(list, service.findAll());
        verify(itemRepo).findAll();
    }

    @Test
    void findById_found_returnsItem() {
        StoreCategoryItem item = helper.createModel(1);
        StoreCategoryItemId id = item.getId();
        when(itemRepo.findById(id)).thenReturn(Optional.of(item));

        assertEquals(item, service.findById(id));
        verify(itemRepo).findById(id);
    }

    @Test
    void findById_notFound_throws() {
        StoreCategoryItemId id = new StoreCategoryItemId(9L,9L);
        when(itemRepo.findById(id)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.findById(id)
        );
        assertEquals("StoreCategoryItem not found with ID: " + id, ex.getMessage());
    }

    @Test
    void save_valid_savesAndReturns() {
        StoreCategoryItem item = helper.createModel(1);
        when(categoryRepo.findById(item.getStoreCategory().getCategoryId()))
            .thenReturn(Optional.of(item.getStoreCategory()));
        when(productRepo.findById(item.getProduct().getProductId()))
            .thenReturn(Optional.of(item.getProduct()));
        when(itemRepo.save(item)).thenReturn(item);

        assertEquals(item, service.saveStoreCategoryItem(item));
        verify(itemRepo).save(item);
    }

    @Test
    void update_valid_savesUpdated() {
        StoreCategoryItem item = helper.createModel(1);
        StoreCategoryItemId id = item.getId();
        when(itemRepo.findById(id)).thenReturn(Optional.of(item));
        when(categoryRepo.findById(item.getStoreCategory().getCategoryId()))
            .thenReturn(Optional.of(item.getStoreCategory()));
        when(productRepo.findById(item.getProduct().getProductId()))
            .thenReturn(Optional.of(item.getProduct()));
        when(itemRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        StoreCategoryItem updated = item.toBuilder().updatedAt(LocalDateTime.now()).build();
        assertEquals(updated, service.updateStoreCategoryItem(id, updated));
        verify(itemRepo).save(updated);
    }

    @Test
    void partialUpdate_appliesField() {
        StoreCategoryItem item = helper.createModel(1);
        StoreCategoryItemId id = item.getId();
        when(itemRepo.findById(id)).thenReturn(Optional.of(item));
        when(itemRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        LocalDateTime now = LocalDateTime.now();
        var result = service.partialUpdateStoreCategoryItem(id, Map.of("updatedAt", now));
        assertEquals(now, result.getUpdatedAt());
    }

    @Test
    void deleteById_existing_invokesSoftDelete() {

        StoreCategoryItem original = helper.createModel(1);
        StoreCategoryItemId id = original.getId();

        when(itemRepo.findById(id))
            .thenReturn(Optional.of(original));
        doNothing().when(itemRepo).delete(original);

        service.deleteById(id);

        verify(itemRepo).findById(id);
        verify(itemRepo).delete(original);
    }
}
