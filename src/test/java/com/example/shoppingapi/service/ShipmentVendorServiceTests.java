package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.ShipmentVendorCreateDTO;
import com.example.shoppingapi.dto.patch.ShipmentVendorPatchDTO;
import com.example.shoppingapi.dto.put.ShipmentVendorPutDTO;
import com.example.shoppingapi.model.ShipmentVendor;
import com.example.shoppingapi.repository.ShipmentVendorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.util.ReflectionUtils;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ShipmentVendorServiceTest {

    @Mock
    private ShipmentVendorRepository vendorRepository;
    @InjectMocks
    private ShipmentVendorService vendorService;

    @Test
    void findAll_returnsAllVendors() {
        List<ShipmentVendor> vendors = List.of(new ShipmentVendor(), new ShipmentVendor());
        when(vendorRepository.findAll()).thenReturn(vendors);

        List<ShipmentVendor> result = vendorService.findAll();

        assertEquals(vendors, result);
        verify(vendorRepository).findAll();
    }

    @Test
    void findById_vendorFound_returnsVendor() {
        Long vendorId = 1L;
        ShipmentVendor vendor = new ShipmentVendor();
        vendor.setVendorId(vendorId);

        when(vendorRepository.findById(vendorId)).thenReturn(Optional.of(vendor));

        ShipmentVendor result = vendorService.findById(vendorId);

        assertSame(vendor, result);
        verify(vendorRepository).findById(vendorId);
    }

    @Test
    void findById_vendorNotFound_throwsResourceNotFound() {
        Long vendorId = 2L;
        when(vendorRepository.findById(vendorId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> vendorService.findById(vendorId)
        );

        assertEquals("Vendor not found with ID: " + vendorId, ex.getMessage());
    }

    @Test
    void saveShipmentVendor_validData_savesAndReturnsVendor() {
        ShipmentVendorCreateDTO createDTO = new ShipmentVendorCreateDTO();
        createDTO.setVendorName("Test Vendor");
        createDTO.setVendorContact("123456789");
        createDTO.setVendorEmail("test@vendor.com");
        createDTO.setOfficialWebsiteUrl("https://vendor.com");

        ShipmentVendor savedVendor = new ShipmentVendor();
        savedVendor.setVendorName(createDTO.getVendorName());
        savedVendor.setVendorContact(createDTO.getVendorContact());
        savedVendor.setVendorEmail(createDTO.getVendorEmail());
        savedVendor.setOfficialWebsiteUrl(createDTO.getOfficialWebsiteUrl());

        when(vendorRepository.save(any(ShipmentVendor.class))).thenReturn(savedVendor);

        ShipmentVendor result = vendorService.saveShipmentVendor(createDTO);

        assertNotNull(result);
        assertEquals(savedVendor.getVendorName(), result.getVendorName());
        assertEquals(savedVendor.getVendorContact(), result.getVendorContact());
        assertEquals(savedVendor.getVendorEmail(), result.getVendorEmail());
        assertEquals(savedVendor.getOfficialWebsiteUrl(), result.getOfficialWebsiteUrl());
        verify(vendorRepository).save(savedVendor);
    }

    @Test
    void updateShipmentVendor_validData_updatesVendor() {
        Long vendorId = 1L;
        ShipmentVendorPutDTO putDTO = new ShipmentVendorPutDTO();
        putDTO.setVendorName("Updated Vendor");
        putDTO.setVendorContact("987654321");
        putDTO.setVendorEmail("updated@vendor.com");
        putDTO.setOfficialWebsiteUrl("https://updatedvendor.com");

        ShipmentVendor existingVendor = new ShipmentVendor();
        existingVendor.setVendorId(vendorId);

        when(vendorRepository.findById(vendorId)).thenReturn(Optional.of(existingVendor));
        when(vendorRepository.save(any(ShipmentVendor.class))).thenReturn(existingVendor);

        ShipmentVendor result = vendorService.updateShipmentVendor(vendorId, putDTO);

        assertEquals("Updated Vendor", result.getVendorName());
        assertEquals("987654321", result.getVendorContact());
        assertEquals("updated@vendor.com", result.getVendorEmail());
        assertEquals("https://updatedvendor.com", result.getOfficialWebsiteUrl());
        verify(vendorRepository).save(existingVendor);
    }

    @Test
    void partialUpdateShipmentVendor_validData_partiallyUpdatesVendor() {
        Long vendorId = 1L;
        ShipmentVendorPatchDTO patchDTO = new ShipmentVendorPatchDTO();
        patchDTO.setVendorName("Patched Vendor");

        ShipmentVendor existingVendor = new ShipmentVendor();
        existingVendor.setVendorId(vendorId);

        when(vendorRepository.findById(vendorId)).thenReturn(Optional.of(existingVendor));
        when(vendorRepository.save(existingVendor)).thenReturn(existingVendor);

        ShipmentVendor result = vendorService.partialUpdateShipmentVendor(vendorId, patchDTO);

        assertEquals("Patched Vendor", result.getVendorName());
        verify(vendorRepository).save(existingVendor);
    }

    @Test
    void deleteShipmentVendor_vendorExists_deletesVendor() {
        Long vendorId = 1L;
        ShipmentVendor vendor = new ShipmentVendor();
        vendor.setVendorId(vendorId);

        when(vendorRepository.findById(vendorId)).thenReturn(Optional.of(vendor));
        doNothing().when(vendorRepository).delete(vendor);

        vendorService.deleteById(vendorId);

        verify(vendorRepository).delete(vendor);
    }

    @Test
    void deleteShipmentVendor_vendorNotFound_throwsResourceNotFound() {
        Long vendorId = 2L;
        when(vendorRepository.findById(vendorId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> vendorService.deleteById(vendorId)
        );

        assertEquals("Vendor not found with ID: " + vendorId, ex.getMessage());
    }
}
