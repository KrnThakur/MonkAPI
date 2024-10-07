package com.example.MonkAPI.pojo.applicable_coupons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicableCoupons {

    List<ValidCoupons> validCouponsList;

}
