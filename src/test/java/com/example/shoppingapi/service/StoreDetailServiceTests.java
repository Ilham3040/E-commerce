package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.StoreDetailCreateDTO;
import com.example.shoppingapi.dto.patch.StoreDetailPatchDTO;
import com.example.shoppingapi.dto.put.StoreDetailPutDTO;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.StoreDetail;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.StoreDetailRepository;
import com.example.shoppingapi.repository.StoreRepository;
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

    @Mock private StoreDetailRepository storeDetailRepository;
    @Mock private StoreRepository storeRepository;
    @InjectMocks private StoreDetailService service;


    private final ModelHelper<StoreDetail> helper =
        ModelHelperFactory.getModelHelper(StoreDetail.class);

    @Test
    void findAll_returnsAll() {
        List<StoreDetail> list = Arrays.asList(helper.createModel(1), helper.createModel(2));
        when(storeDetailRepository.findAll()).thenReturn(list);
        assertEquals(list, service.findAll());
    }

    @Test
    void findById_found_returnsDetail() {
        StoreDetail detail = helper.createModel(1);
        when(storeDetailRepository.findStoreDetailbyProductId(1L)).thenReturn(Optional.of(detail));
        assertEquals(detail, service.getStoreDetailByStoreId(1L));
    }

    @Test
    void findById_notFound_throws() {
        when(storeDetailRepository.findStoreDetailbyProductId(2L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.getStoreDetailByStoreId(2L)
        );
        assertEquals("StoreDetail not found with ID: 2", ex.getMessage());
    }

    @Test
    void save_savesAndReturns() {
        StoreDetail detail = helper.createModel(1);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(detail.getStore()));

        StoreDetailCreateDTO storeDetailCreateDTO = new StoreDetailCreateDTO();
        storeDetailCreateDTO.setStoreId(detail.getStore().getStoreId());
        storeDetailCreateDTO.setAddress(detail.getAddress());
        storeDetailCreateDTO.setDescription(detail.getDescription());

        StoreDetail toSave = StoreDetail.builder()
                .store(Store.builder().storeId(detail.getStore().getStoreId()).build())
                .description(detail.getDescription())
                .address(detail.getAddress())
                .build();

        when(storeDetailRepository.save(toSave)).thenReturn(detail);
        assertEquals(detail, service.saveStoreDetail(storeDetailCreateDTO));

        verify(storeDetailRepository).save(toSave);
        verify(storeRepository).findById(1L);
    }

    @Test
    void update_valid_savesUpdated() {
        StoreDetail detail = helper.createModel(1);

        StoreDetailPutDTO storeDetailPutDTO = new StoreDetailPutDTO();
        storeDetailPutDTO.setAddress("never mind");
        storeDetailPutDTO.setDescription("whatever you want");

        when(storeDetailRepository.findStoreDetailbyProductId(1L)).thenReturn(Optional.of(detail));
        when(storeDetailRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        StoreDetail result = service.updateStoreDetail(1L, storeDetailPutDTO);
        assertEquals(result.getDescription(),storeDetailPutDTO.getDescription());
        assertEquals(result.getAddress(),storeDetailPutDTO.getAddress());
        verify(storeDetailRepository).findStoreDetailbyProductId(1L);
        verify(storeDetailRepository).save(detail);
    }

    @Test
    void partialUpdate_appliesUpdates() {
        StoreDetail detail = helper.createModel(1);

        StoreDetailPatchDTO storeDetailPatchDTO = new StoreDetailPatchDTO();
        storeDetailPatchDTO.setDescription("whatever you want");

        when(storeDetailRepository.findStoreDetailbyProductId(1L)).thenReturn(Optional.of(detail));
        when(storeDetailRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        StoreDetail result = service.partiallyUpdateStoreDetail(1L, storeDetailPatchDTO);

        assertEquals(result.getDescription(),"whatever you want");
        verify(storeDetailRepository).findStoreDetailbyProductId(1L);
        verify(storeDetailRepository).save(detail);
    }

    @Test
    void deleteById_existing_invokesSoftDelete() {

        StoreDetail original = helper.createModel(1);

        when(storeDetailRepository.findStoreDetailbyProductId(1L))
            .thenReturn(Optional.of(original));
        doNothing().when(storeDetailRepository).delete(original);

        service.deleteById(1L);

        verify(storeDetailRepository).findStoreDetailbyProductId(1L);
        verify(storeDetailRepository).delete(original);
    }
}
