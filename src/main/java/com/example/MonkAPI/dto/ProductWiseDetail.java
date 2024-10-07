package com.example.MonkAPI.dto;

import jakarta.persistence.Entity;



@Entity
public class ProductWiseDetail extends CouponDetail {

    private Long productId;
    private Long discount;

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }
}

