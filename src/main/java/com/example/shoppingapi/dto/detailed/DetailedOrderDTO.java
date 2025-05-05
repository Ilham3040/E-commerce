package com.example.shoppingapi.dto.detailed;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailedOrderDTO {
    private Long orderId;
    private Long userId;
    private Long productId;
    private LocalDateTime orderDate;
    private String status;
}
