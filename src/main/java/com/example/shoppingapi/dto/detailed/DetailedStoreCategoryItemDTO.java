package com.example.shoppingapi.dto.detailed;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailedStoreCategoryItemDTO {
    private Long categoryId;
    private Long productId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
