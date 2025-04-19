package com.example.shoppingapi.service;

import com.example.shoppingapi.model.ShipmentVendor;
import com.example.shoppingapi.repository.ShipmentVendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShipmentVendorService {

    private final ShipmentVendorRepository vendorRepo;

    public List<ShipmentVendor> findAll() {
        return vendorRepo.findAll();
    }

    public ShipmentVendor findById(Long id) {
        return vendorRepo.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("ShipmentVendor not found with ID: " + id));
    }

    public ShipmentVendor saveShipmentVendor(ShipmentVendor vendor) {
        return vendorRepo.save(vendor);
    }

    public ShipmentVendor updateShipmentVendor(Long id, ShipmentVendor vendor) {
        findById(id);  // throws if missing
        vendor.setVendorId(id);
        return vendorRepo.save(vendor);
    }

    public ShipmentVendor partialUpdateShipmentVendor(Long id, Map<String, Object> updates) {
        ShipmentVendor existing = findById(id);

        BeanWrapper wrapper = new BeanWrapperImpl(existing);
        updates.forEach(wrapper::setPropertyValue);

        return vendorRepo.save(existing);
    }

    public ShipmentVendor softDeleteShipmentVendor(Long id) {
        ShipmentVendor existing = findById(id);
        existing.setDeletedAt(LocalDateTime.now());
        return vendorRepo.save(existing);
    }
}
