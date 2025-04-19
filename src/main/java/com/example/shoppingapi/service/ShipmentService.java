package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Shipment;
import com.example.shoppingapi.model.ShipmentId;
import com.example.shoppingapi.repository.OrderRepository;
import com.example.shoppingapi.repository.ShipmentRepository;
import com.example.shoppingapi.repository.ShipmentVendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository       shipmentRepository;
    private final ShipmentVendorRepository vendorRepository;
    private final OrderRepository          orderRepository;

    public List<Shipment> findAll() {
        return shipmentRepository.findAll();
    }

    public Shipment findById(ShipmentId id) {
        return shipmentRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Shipment not found with ID: " + id));
    }

    public Shipment saveShipment(Shipment shipment) {
        vendorRepository.findById(shipment.getShipmentVendor().getVendorId())
            .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        orderRepository.findById(shipment.getOrder().getOrderId())
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return shipmentRepository.save(shipment);
    }

    public Shipment updateShipment(ShipmentId id, Shipment shipment) {
        if (!id.equals(shipment.getId())) {
            throw new IllegalArgumentException("ID parameter and ID in body must match");
        }
        findById(id);
        vendorRepository.findById(shipment.getShipmentVendor().getVendorId())
            .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        orderRepository.findById(shipment.getOrder().getOrderId())
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        shipment.setId(id);
        return shipmentRepository.save(shipment);
    }

    public Shipment partialUpdateShipment(ShipmentId id, Map<String, Object> updates) {
        Shipment existing = findById(id);

        BeanWrapper wrapper = new BeanWrapperImpl(existing);
        updates.forEach(wrapper::setPropertyValue);

        return shipmentRepository.save(existing);
    }
}
