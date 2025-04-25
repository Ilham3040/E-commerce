//package com.example.shoppingapi.controller;
//
//import com.example.shoppingapi.dto.request.ProductReviewCreateDTO;
//import com.example.shoppingapi.dto.response.ApiResponse;
//import com.example.shoppingapi.dto.response.ProductReviewDTO;
//import com.example.shoppingapi.model.*;
//import com.example.shoppingapi.service.ProductReviewService;
//
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//
//
//@RestController
//@RequestMapping("/api/productsreview")
//@RequiredArgsConstructor
//public class ProductReviewController {
//    private final ProductReviewService productReviewService;
//
//    @GetMapping
//    public ApiResponse<List<ProductReviewDTO>> getAllProductReview(){
//        List<ProductReviewDTO> datas = productReviewService.findAll()
//            .stream()
//            .map(review -> new ProductReviewDTO(review.getReviewId(),
//                                                review.getUser().getUserId(),
//                                                review.getProduct().getProductId()))
//            .collect(Collectors.toList());
//            return new ApiResponse<>("Successfully fetch all data",datas,HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ApiResponse<ProductReview> getProductById(@PathVariable Long id){
//        ProductReview review = productReviewService.findById(id);
//        return new ApiResponse<>("Successfully fetch data", review,HttpStatus.OK);
//    }
//
//    @PostMapping
//    public ApiResponse<ProductReviewDTO> createProductReview(@Validated @RequestBody ProductReviewCreateDTO dto){
//        ProductReview wannabe = ProductReview.builder()
//            .user(User.builder().userId(dto.getUserId()).build())
//            .product(Product.builder().productId(dto.getProductId()).build())
//            .starRating(dto.getStarRating())
//            .build();
//
//        ProductReview created = productReviewService.saveProductReview(wannabe);
//        return new ApiResponse<>("Succesfully created product review",
//            new ProductReviewDTO(created.getReviewId(), created.getUser().getUserId(), created.getProduct().getProductId()),HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}")
//    public ApiResponse<ProductReviewDTO> updateProductReview(@PathVariable Long id,@Validated @RequestBody ProductReviewCreateDTO dto) {
//        ProductReview wannabe = ProductReview.builder()
//            .reviewId(id)
//            .user(User.builder().userId(dto.getUserId()).build())
//            .product(Product.builder().productId(dto.getProductId()).build())
//            .starRating(dto.getStarRating())
//            .build();
//
//            ProductReview updated = productReviewService.updateProductReview(id,wannabe);
//            return new ApiResponse<>("Succesfully updated product review",
//                new ProductReviewDTO(updated.getReviewId(), updated.getUser().getUserId(), updated.getProduct().getProductId()),HttpStatus.OK);
//    }
//
//    @PatchMapping("/{id}")
//    public ApiResponse<ProductReviewDTO> updatePartiallyProductReview(@PathVariable Long id, @RequestBody Map<String,Object> updates){
//        ProductReview updated = productReviewService.partialUpdateProductReview(id,updates);
//            return new ApiResponse<>("Succesfully updated product review",
//                new ProductReviewDTO(updated.getReviewId(), updated.getUser().getUserId(), updated.getProduct().getProductId()),HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ApiResponse<Void> deleteProductReview(@PathVariable Long id){
//        productReviewService.deleteById(id);
//        return new ApiResponse<>("Successfully deleted product review", null,HttpStatus.NO_CONTENT);
//    }
//}
