package com.example.shoppingapi.dto.detailed;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailedProductVariantDTO {
    private Long variantId;
    private Long productId;
    private String variantName;
    private BigDecimal price;
    private Integer stockQuantity;
    private Integer totalSold;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
