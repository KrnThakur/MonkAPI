package com.example.MonkAPI.dto;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class CartWiseDetail extends CouponDetail {

    private int threshold;
    private int discount;

}

