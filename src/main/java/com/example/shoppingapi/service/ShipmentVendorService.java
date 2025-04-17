package com.example.shoppingapi.service;

import com.example.shoppingapi.model.ShipmentVendor;
import com.example.shoppingapi.repository.ShipmentVendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShipmentVendorService {

    @Autowired
    private ShipmentVendorRepository shipmentVendorRepository;

    public List<ShipmentVendor> findAll() {
        return shipmentVendorRepository.findAll();
    }

    public Optional<ShipmentVendor> findById(Long id) {
        return shipmentVendorRepository.findById(id);
    }

    public ShipmentVendor saveShipmentVendor(ShipmentVendor shipmentVendor) {
        return shipmentVendorRepository.save(shipmentVendor);
    }

    public ShipmentVendor updateShipmentVendor(Long id, ShipmentVendor shipmentVendor) {
        Optional<ShipmentVendor> existingOpt = shipmentVendorRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("ShipmentVendor not found with ID: " + id);
        }
        shipmentVendor.setVendorId(id);
        return shipmentVendorRepository.save(shipmentVendor);
    }

    public ShipmentVendor partialUpdateShipmentVendor(Long id, Map<String, Object> updates) {
        Optional<ShipmentVendor> existingOpt = shipmentVendorRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("Shipment Vendor not found with ID: " + id);
        }
        ShipmentVendor shipmentVendor = existingOpt.get();
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(ShipmentVendor.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, shipmentVendor, value);
            }
        });
        return shipmentVendorRepository.save(shipmentVendor);
    }

    public ShipmentVendor deleteById(Long id) {
        ShipmentVendor shipmentVendor = shipmentVendorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Shipment Vendor not found with ID: " + id));

        shipmentVendor.setDeletedAt(LocalDateTime.now());
        shipmentVendorRepository.save(shipmentVendor);
        return shipmentVendor;
    }
}
