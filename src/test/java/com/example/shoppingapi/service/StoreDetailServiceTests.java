package com.example.shoppingapi.service;

import com.example.shoppingapi.model.StoreDetail;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.StoreDetailRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreDetailServiceTest {

    @Mock private StoreDetailRepository detailRepo;
    @InjectMocks private StoreDetailService service;

    private final ModelHelper<StoreDetail> helper =
        ModelHelperFactory.getModelHelper(StoreDetail.class);

    @Test
    void findAll_returnsAll() {
        List<StoreDetail> list = Arrays.asList(helper.createModel(1), helper.createModel(2));
        when(detailRepo.findAll()).thenReturn(list);
        assertEquals(list, service.findAll());
    }

    @Test
    void findById_found_returnsDetail() {
        StoreDetail d = helper.createModel(1);
        when(detailRepo.findById(1L)).thenReturn(Optional.of(d));
        assertEquals(d, service.findById(1L));
    }

    @Test
    void findById_notFound_throws() {
        when(detailRepo.findById(2L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.findById(2L)
        );
        assertEquals("StoreDetail not found with ID: 2", ex.getMessage());
    }

    @Test
    void save_savesAndReturns() {
        StoreDetail d = helper.createModel(1);
        when(detailRepo.save(d)).thenReturn(d);
        assertEquals(d, service.saveStoreDetail(d));
    }

    @Test
    void update_valid_savesUpdated() {
        StoreDetail d = helper.createModel(1);
        when(detailRepo.findById(1L)).thenReturn(Optional.of(d));
        when(detailRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        StoreDetail upd = d.toBuilder().address("X").build();
        assertEquals(upd, service.updateStoreDetail(1L, upd));
    }

    @Test
    void partialUpdate_appliesUpdates() {
        StoreDetail d = helper.createModel(1);
        when(detailRepo.findById(1L)).thenReturn(Optional.of(d));
        when(detailRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        StoreDetail res = service.partialUpdateStoreDetail(1L, Map.of("address","Y","followerCount",50));
        assertEquals("Y", res.getAddress());
        assertEquals(50, res.getFollowerCount());
    }

    @Test
    void deleteById_existing_invokesSoftDelete() {

        StoreDetail original = helper.createModel(1);

        when(detailRepo.findById(1L))
            .thenReturn(Optional.of(original));
        doNothing().when(detailRepo).delete(original);

        service.deleteById(1L);

        verify(detailRepo).findById(1L);
        verify(detailRepo).delete(original);
    }
}
