// package com.example.shoppingapi.controller;

// import com.example.shoppingapi.dto.response.ApiResponse;
// import com.example.shoppingapi.dto.response.StoreRoleDTO;
// import com.example.shoppingapi.model.StoreRole;
// import com.example.shoppingapi.service.StoreRoleService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.*;

// @RestController
// @RequestMapping("/api/store-roles")
// public class StoreRoleController {

//     @Autowired
//     private StoreRoleService storeRoleService;

//     @GetMapping
//     public List<StoreRole> getAllStoreRoles() {
//         return storeRoleService.findAll();
//     }

//     @GetMapping("/{userId}/{storeId}")
//     public Optional<StoreRole> getStoreRoleById(@PathVariable Long userId,
//                                                 @PathVariable Long storeId) {
//         return storeRoleService.findById(userId, storeId);
//     }

//     @PostMapping
//     public ResponseEntity<StoreRoleDTO> createStoreRole(@RequestBody StoreRole storeRole) {
//         StoreRole savedStoreRole = storeRoleService.saveStoreRole(storeRole);
//         StoreRoleDTO dto = new StoreRoleDTO(savedStoreRole.getId().getUserId(),
//                 savedStoreRole.getId().getStoreId(), savedStoreRole.getRole());
//         return ResponseEntity.ok(dto);
//     }

//     @PutMapping("/{userId}/{storeId}")
//     public ResponseEntity<ApiResponse<StoreRoleDTO>> updateStoreRole(@PathVariable Long userId,
//                                                                      @PathVariable Long storeId,
//                                                                      @RequestBody StoreRole storeRole) {
//         try {
//             StoreRole updatedStoreRole = storeRoleService.updateStoreRole(userId, storeId, storeRole);
//             StoreRoleDTO dto = new StoreRoleDTO(updatedStoreRole.getId().getUserId(),
//                     updatedStoreRole.getId().getStoreId(), updatedStoreRole.getRole());
//             ApiResponse<StoreRoleDTO> response = new ApiResponse<>("StoreRole successfully updated", dto);
//             return ResponseEntity.ok(response);
//         } catch (IllegalArgumentException e) {
//             ApiResponse<StoreRoleDTO> response = new ApiResponse<>(e.getMessage(), null);
//             return ResponseEntity.badRequest().body(response);
//         }
//     }

//     @PatchMapping("/{userId}/{storeId}")
//     public ResponseEntity<ApiResponse<StoreRoleDTO>> partialUpdateStoreRole(@PathVariable Long userId,
//                                                                             @PathVariable Long storeId,
//                                                                             @RequestBody Map<String, Object> updates) {
//         try {
//             StoreRole updatedStoreRole = storeRoleService.partialUpdateStoreRole(userId, storeId, updates);
//             StoreRoleDTO dto = new StoreRoleDTO(updatedStoreRole.getId().getUserId(),
//                     updatedStoreRole.getId().getStoreId(), updatedStoreRole.getRole());
//             ApiResponse<StoreRoleDTO> response = new ApiResponse<>("StoreRole successfully updated", dto);
//             return ResponseEntity.ok(response);
//         } catch (Exception e) {
//             ApiResponse<StoreRoleDTO> response = new ApiResponse<>("Error updating StoreRole: " + e.getMessage(), null);
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//         }
//     }

//     @DeleteMapping("/{userId}/{storeId}")
//     public void deleteByIdRole(@PathVariable Long userId,
//                                 @PathVariable Long storeId) {
//         storeRoleService.deleteById(userId, storeId);
//     }
// }
