package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.ShipmentVendorDTO;
import com.example.shoppingapi.model.ShipmentVendor;
import com.example.shoppingapi.service.ShipmentVendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/shipment-vendors")
public class ShipmentVendorController {

    @Autowired
    private ShipmentVendorService shipmentVendorService;

    @GetMapping
    public List<ShipmentVendor> getAllShipmentVendors() {
        return shipmentVendorService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<ShipmentVendor> getShipmentVendorById(@PathVariable Long id) {
        return shipmentVendorService.findById(id);
    }

    @PostMapping
    public ResponseEntity<ShipmentVendorDTO> createShipmentVendor(@RequestBody ShipmentVendor shipmentVendor) {
        ShipmentVendor savedShipmentVendor = shipmentVendorService.saveShipmentVendor(shipmentVendor);
        ShipmentVendorDTO dto = new ShipmentVendorDTO(savedShipmentVendor.getVendorId());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ShipmentVendorDTO>> updateShipmentVendor(@PathVariable Long id,
                                                                               @RequestBody ShipmentVendor shipmentVendor) {
        try {
            ShipmentVendor updatedShipmentVendor = shipmentVendorService.updateShipmentVendor(id, shipmentVendor);
            ShipmentVendorDTO dto = new ShipmentVendorDTO(updatedShipmentVendor.getVendorId());
            ApiResponse<ShipmentVendorDTO> response = new ApiResponse<>("ShipmentVendor successfully updated", dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<ShipmentVendorDTO> response = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ShipmentVendorDTO>> partialUpdateShipmentVendor(@PathVariable Long id,
                                                                                      @RequestBody Map<String, Object> updates) {
        try {
            ShipmentVendor updatedShipmentVendor = shipmentVendorService.partialUpdateShipmentVendor(id, updates);
            ShipmentVendorDTO dto = new ShipmentVendorDTO(updatedShipmentVendor.getVendorId());
            ApiResponse<ShipmentVendorDTO> response = new ApiResponse<>("ShipmentVendor successfully updated", dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<ShipmentVendorDTO> response = new ApiResponse<>("Error updating ShipmentVendor: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteShipmentVendor(@PathVariable Long id) {
        shipmentVendorService.deleteById(id);
    }
}
