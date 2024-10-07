package com.example.MonkAPI.pojo.applicable_coupons;

import lombok.Data;

@Data
public class ValidCoupons {
    private Long coupon_id;
    private String type;
    private Long discount;
}
