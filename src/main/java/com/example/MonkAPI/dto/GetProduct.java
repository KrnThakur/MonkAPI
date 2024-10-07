package com.example.MonkAPI.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
 public  class GetProduct {
    private Long productId;
    private Long quantity;

    public GetProduct() {
        // No-argument constructor
    }

    public GetProduct(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
