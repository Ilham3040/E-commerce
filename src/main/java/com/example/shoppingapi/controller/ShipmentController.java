// package com.example.shoppingapi.controller;

// import com.example.shoppingapi.dto.response.ApiResponse;
// import com.example.shoppingapi.dto.response.ShipmentDTO;
// import com.example.shoppingapi.model.Shipment;
// import com.example.shoppingapi.service.ShipmentService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.*;

// @RestController
// @RequestMapping("/api/shipments")
// public class ShipmentController {

//     @Autowired
//     private ShipmentService shipmentService;

//     @GetMapping
//     public List<Shipment> getAllShipments() {
//         return shipmentService.findAll();
//     }

//     @GetMapping("/{id}")
//     public Optional<Shipment> getShipmentById(@PathVariable Long id) {
//         return shipmentService.findById(id);
//     }

//     @PostMapping
//     public ResponseEntity<ShipmentDTO> createShipment(@RequestBody Shipment shipment) {
//         Shipment savedShipment = shipmentService.saveShipment(shipment);
//         ShipmentDTO dto = new ShipmentDTO(savedShipment.getShipmentId(),
//                 savedShipment.getShipmentVendor().getVendorId(),
//                 savedShipment.getOrder().getOrderId());
//         return ResponseEntity.ok(dto);
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<ApiResponse<ShipmentDTO>> updateShipment(@PathVariable Long id,
//                                                                    @RequestBody Shipment shipment) {
//         try {
//             Shipment updatedShipment = shipmentService.updateShipment(id, shipment);
//             ShipmentDTO dto = new ShipmentDTO(updatedShipment.getShipmentId(),
//                     updatedShipment.getShipmentVendor().getVendorId(),
//                     updatedShipment.getOrder().getOrderId());
//             ApiResponse<ShipmentDTO> response = new ApiResponse<>("Shipment successfully updated", dto);
//             return ResponseEntity.ok(response);
//         } catch (IllegalArgumentException e) {
//             ApiResponse<ShipmentDTO> response = new ApiResponse<>(e.getMessage(), null);
//             return ResponseEntity.badRequest().body(response);
//         }
//     }

//     @PatchMapping("/{id}")
//     public ResponseEntity<ApiResponse<ShipmentDTO>> partialUpdateShipment(@PathVariable Long id,
//                                                                           @RequestBody Map<String, Object> updates) {
//         try {
//             Shipment updatedShipment = shipmentService.partialUpdateShipment(id, updates);
//             ShipmentDTO dto = new ShipmentDTO(updatedShipment.getShipmentId(),
//                     updatedShipment.getShipmentVendor().getVendorId(),
//                     updatedShipment.getOrder().getOrderId());
//             ApiResponse<ShipmentDTO> response = new ApiResponse<>("Shipment successfully updated", dto);
//             return ResponseEntity.ok(response);
//         } catch (Exception e) {
//             ApiResponse<ShipmentDTO> response = new ApiResponse<>("Error updating Shipment: " + e.getMessage(), null);
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//         }
//     }

//     @DeleteMapping("/{id}")
//     public void deleteShipment(@PathVariable Long id) {
//         shipmentService.deleteById(id);
//     }
// }
