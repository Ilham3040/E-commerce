package com.example.shoppingapi.dto.detailed;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailedProductDetailDTO {
    private Long productDetailId;
    private Long productId;
    private String description;
    private List<String> images;
    private BigDecimal reviewRating;
    private Integer totalSold;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
