package com.example.shoppingapi.dto.detailed;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailedStoreDetailDTO {
    private Long storeDetailId;
    private Long storeId;
    private String address;
    private Integer totalReview;
    private Integer totalProducts;
    private String description;
    private List<String> attachmentUrls;
    private Integer followerCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
