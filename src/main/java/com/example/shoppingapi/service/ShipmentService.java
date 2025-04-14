package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Shipment;
import com.example.shoppingapi.model.ShipmentId;
import com.example.shoppingapi.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    public List<Shipment> findAll() {
        return shipmentRepository.findAll();
    }

    public Optional<Shipment> findById(ShipmentId id) {
        return shipmentRepository.findById(id);
    }

    public Shipment saveShipment(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    public Shipment updateShipment(ShipmentId id, Shipment shipment) {
        Optional<Shipment> existingOpt = shipmentRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("Shipment not found with ID: " + id);
        }
        // Set the composite key
        shipment.setId(id);
        return shipmentRepository.save(shipment);
    }

    public Shipment partialUpdateShipment(ShipmentId id, Map<String, Object> updates) {
        Optional<Shipment> existingOpt = shipmentRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("Shipment not found with ID: " + id);
        }
        Shipment shipment = existingOpt.get();
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Shipment.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, shipment, value);
            }
        });
        return shipmentRepository.save(shipment);
    }

    public void deleteById(ShipmentId id) {
        shipmentRepository.deleteById(id);
    }
}
