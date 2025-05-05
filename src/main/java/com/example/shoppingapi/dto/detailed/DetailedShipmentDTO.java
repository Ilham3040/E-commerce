package com.example.shoppingapi.dto.detailed;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailedShipmentDTO {
    private Long vendorId;
    private Long orderId;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
