package com.example.shoppingapi.dto.detailed;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailedProductReviewDTO {
    private Long reviewId;
    private Long productId;
    private Long userId;
    private String description;
    private List<String> images;
    private Integer starRating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAT;
}
