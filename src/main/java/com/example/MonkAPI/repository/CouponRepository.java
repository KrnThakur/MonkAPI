package com.example.MonkAPI.repository;

import com.example.MonkAPI.dto.BxGyDetail;
import com.example.MonkAPI.dto.CartWiseDetail;
import com.example.MonkAPI.dto.Coupon;
import com.example.MonkAPI.dto.GetProduct;
import com.example.MonkAPI.pojo.dataFromDb.ResultBxGy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByType(String type);

//    @Query("(SELECT b FROM BxGyDetail b JOIN b.buyProducts p WHERE p.productId IN :productIds)")
//    BxGyDetail findByTypeAndProductIds(@Param("productIds") Long productIds);
//
//    @Query("SELECT c FROM Coupon c JOIN c.bxGyDetail b JOIN b.buyProducts p WHERE p.productId IN :productIds")
//    List<Coupon> findByTypeAndProductId(@Param("productIds") List<Long> productIds);



}

