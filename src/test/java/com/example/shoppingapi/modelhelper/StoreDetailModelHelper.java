//package com.example.shoppingapi.modelhelper;
//
//import com.example.shoppingapi.model.StoreDetail;
//import com.example.shoppingapi.model.Store;
//import lombok.NoArgsConstructor;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@NoArgsConstructor
//public class StoreDetailModelHelper implements ModelHelper<StoreDetail> {
//
//    private final ModelHelper<Store> storeHelper = ModelHelperFactory.getModelHelper(Store.class);
//
//    @Override
//    public StoreDetail createModel(Integer num) {
//        if (num == 1) {
//            return StoreDetail.builder()
//                .storeDetailId(1L)
//                .store(storeHelper.createModel(1))
//                .address("123 Main St, Example City")
//                .review(BigDecimal.valueOf(4.5))
//                .totalProducts(150)
//                .description("A great pet shop with a variety of products")
//                .followerCount(120)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//        } else {
//            return StoreDetail.builder()
//                .storeDetailId(2L)
//                .store(storeHelper.createModel(2))
//                .address("456 Market Rd, Sample City")
//                .review(BigDecimal.valueOf(3.8))
//                .totalProducts(200)
//                .description("A well-known bookstore with an extensive collection")
//                .followerCount(80)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//        }
//    }
//}
