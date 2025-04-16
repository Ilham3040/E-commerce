// package com.example.shoppingapi.controller;

// import com.example.shoppingapi.dto.response.ApiResponse;
// import com.example.shoppingapi.dto.response.UserCartDTO;
// import com.example.shoppingapi.model.UserCart;
// import com.example.shoppingapi.service.UserCartService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.*;

// @RestController
// @RequestMapping("/api/user-carts")
// public class UserCartController {

//     @Autowired
//     private UserCartService userCartService;

//     @GetMapping
//     public List<UserCart> getAllUserCarts() {
//         return userCartService.findAll();
//     }

//     @GetMapping("/{id}")
//     public Optional<UserCart> getUserCartById(@PathVariable Long id) {
//         return userCartService.findById(id);
//     }

//     @PostMapping
//     public ResponseEntity<UserCartDTO> createUserCart(@RequestBody UserCart userCart) {
//         UserCart savedUserCart = userCartService.saveUserCart(userCart);
//         UserCartDTO dto = new UserCartDTO(savedUserCart.getCartId(),
//                 savedUserCart.getUser().getUserId(), savedUserCart.getProduct().getProductId());
//         return ResponseEntity.ok(dto);
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<ApiResponse<UserCartDTO>> updateUserCart(@PathVariable Long id,
//                                                                    @RequestBody UserCart userCart) {
//         try {
//             UserCart updatedUserCart = userCartService.updateUserCart(id, userCart);
//             UserCartDTO dto = new UserCartDTO(updatedUserCart.getCartId(),
//                     updatedUserCart.getUser().getUserId(), updatedUserCart.getProduct().getProductId());
//             ApiResponse<UserCartDTO> response = new ApiResponse<>("UserCart successfully updated", dto);
//             return ResponseEntity.ok(response);
//         } catch (IllegalArgumentException e) {
//             ApiResponse<UserCartDTO> response = new ApiResponse<>(e.getMessage(), null);
//             return ResponseEntity.badRequest().body(response);
//         }
//     }

//     @PatchMapping("/{id}")
//     public ResponseEntity<ApiResponse<UserCartDTO>> partialUpdateUserCart(@PathVariable Long id,
//                                                                           @RequestBody Map<String, Object> updates) {
//         try {
//             UserCart updatedUserCart = userCartService.partialUpdateUserCart(id, updates);
//             UserCartDTO dto = new UserCartDTO(updatedUserCart.getCartId(),
//                     updatedUserCart.getUser().getUserId(), updatedUserCart.getProduct().getProductId());
//             ApiResponse<UserCartDTO> response = new ApiResponse<>("UserCart successfully updated", dto);
//             return ResponseEntity.ok(response);
//         } catch (Exception e) {
//             ApiResponse<UserCartDTO> response = new ApiResponse<>("Error updating UserCart: " + e.getMessage(), null);
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//         }
//     }

//     @DeleteMapping("/{id}")
//     public void deleteByIdCart(@PathVariable Long id) {
//         userCartService.deleteById(id);
//     }
// }
