package com.example.shoppingapi.dto.detailed;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailedProductDTO {
    private Long productId;
    private String productName;
    private Long storeId;
    private BigDecimal price;
    private Integer totalReviews;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
