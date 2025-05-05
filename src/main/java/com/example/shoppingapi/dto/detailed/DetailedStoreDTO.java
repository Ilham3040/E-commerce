package com.example.shoppingapi.dto.detailed;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailedStoreDTO {
    private Long storeId;
    private String storeName;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
