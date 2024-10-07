package com.example.MonkAPI.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
public class BuyProduct {
    private Long productId;
    private Long quantity;

    public BuyProduct() {
        // No-argument constructor
    }

    public BuyProduct(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
    // Getters and Setters
}
