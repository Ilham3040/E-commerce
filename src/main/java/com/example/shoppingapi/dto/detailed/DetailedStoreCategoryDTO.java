package com.example.shoppingapi.dto.detailed;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailedStoreCategoryDTO {
    private Long categoryId;
    private String categoryName;
    private Long storeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
