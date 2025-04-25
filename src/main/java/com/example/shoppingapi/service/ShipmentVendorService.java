package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.ShipmentVendorCreateDTO;
import com.example.shoppingapi.dto.patch.ShipmentVendorPatchDTO;
import com.example.shoppingapi.dto.put.ShipmentVendorPutDTO;
import com.example.shoppingapi.model.ShipmentVendor;
import com.example.shoppingapi.repository.ShipmentVendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShipmentVendorService {

    private final ShipmentVendorRepository vendorRepository;

    // Get all vendors
    public List<ShipmentVendor> findAll() {
        return vendorRepository.findAll();
    }

    // Get a vendor by ID
    public ShipmentVendor findById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with ID: " + id));
    }

    // Create a new shipment vendor
    public ShipmentVendor saveShipmentVendor(ShipmentVendorCreateDTO createDTO) {
        ShipmentVendor newVendor = new ShipmentVendor();
        newVendor.setVendorName(createDTO.getVendorName());
        newVendor.setVendorContact(createDTO.getVendorContact());
        newVendor.setVendorEmail(createDTO.getVendorEmail());
        newVendor.setOfficialWebsiteUrl(createDTO.getOfficialWebsiteUrl());

        return vendorRepository.save(newVendor);
    }

    // Update shipment vendor with PUT using ReflectionUtils
    public ShipmentVendor updateShipmentVendor(Long id, ShipmentVendorPutDTO putDTO) {
        ShipmentVendor existingVendor = findById(id);

        // Using ReflectionUtils to update fields dynamically
        ReflectionUtils.doWithFields(ShipmentVendorPutDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(putDTO);
            if (value != null) {
                Field vendorField = ReflectionUtils.findField(ShipmentVendor.class, field.getName());
                if (vendorField != null) {
                    vendorField.setAccessible(true);
                    vendorField.set(existingVendor, value);
                }
            }
        });

        return vendorRepository.save(existingVendor);
    }

    public ShipmentVendor partialUpdateShipmentVendor(Long id, ShipmentVendorPatchDTO patchDTO) {
        ShipmentVendor existingVendor = findById(id);

        // Using ReflectionUtils to update only non-null fields
        ReflectionUtils.doWithFields(ShipmentVendorPatchDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(patchDTO);
            if (value != null) {
                Field vendorField = ReflectionUtils.findField(ShipmentVendor.class, field.getName());
                if (vendorField != null) {
                    vendorField.setAccessible(true);
                    vendorField.set(existingVendor, value);
                }
            }
        });

        return vendorRepository.save(existingVendor);
    }

    // Delete vendor by ID
    public void deleteById(Long id) {
        ShipmentVendor existingVendor = findById(id);
        vendorRepository.delete(existingVendor);
    }
}
