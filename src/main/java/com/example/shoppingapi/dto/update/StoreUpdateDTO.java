package com.example.shoppingapi.dto.update;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StoreUpdateDTO {
    @Size(max = 100, message = "Store name must not exceed 100 characters")
    private String storeName;
}
