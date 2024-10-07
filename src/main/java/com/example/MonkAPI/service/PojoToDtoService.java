package com.example.MonkAPI.service;

import com.example.MonkAPI.dto.*;
import com.example.MonkAPI.pojo.bxgy.Coupon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for mapping POJO to DTO objects.
 */
@Service
@Slf4j
public class PojoToDtoService {

    /**
     * Maps a CartWise coupon POJO to a DTO.
     *
     * @param coupon1 the CartWise coupon POJO.
     * @return the mapped DTO.
     */
    public com.example.MonkAPI.dto.Coupon mapToCartWise(com.example.MonkAPI.pojo.cart_wise.Coupon coupon1) {
        log.info("Mapping CartWise coupon POJO to DTO: {}", coupon1);
        com.example.MonkAPI.dto.Coupon coupon = new com.example.MonkAPI.dto.Coupon();
        coupon.setType(coupon1.getType());
        CartWiseDetail cartWiseDetail = new CartWiseDetail();
        cartWiseDetail.setDiscount(coupon1.getDetails().getDiscount());
        cartWiseDetail.setThreshold(coupon1.getDetails().getThreshold());
        coupon.setDetails(cartWiseDetail);
        log.info("Mapped CartWise coupon DTO: {}", coupon);
        return coupon;
    }

    /**
     * Maps a ProductWise coupon POJO to a DTO.
     *
     * @param coupon1 the ProductWise coupon POJO.
     * @return the mapped DTO.
     */
    public com.example.MonkAPI.dto.Coupon mapToProductWise(com.example.MonkAPI.pojo.productwise.Coupon coupon1) {
        log.info("Mapping ProductWise coupon POJO to DTO: {}", coupon1);
        com.example.MonkAPI.dto.Coupon coupon = new com.example.MonkAPI.dto.Coupon();
        coupon.setType(coupon1.getType());
        ProductWiseDetail productWiseDetail = new ProductWiseDetail();
        productWiseDetail.setDiscount(coupon1.getDetails().getDiscount());
        productWiseDetail.setProductId((long) coupon1.getDetails().getProductId());
        coupon.setDetails(productWiseDetail);
        log.info("Mapped ProductWise coupon DTO: {}", coupon);
        return coupon;
    }

    /**
     * Maps a BxGy coupon POJO to a DTO.
     *
     * @param coupon1 the BxGy coupon POJO.
     * @return the mapped DTO.
     */
    public com.example.MonkAPI.dto.Coupon mapToBxGy(Coupon coupon1) {
        log.info("Mapping BxGy coupon POJO to DTO: {}", coupon1);
        com.example.MonkAPI.dto.Coupon coupon = new com.example.MonkAPI.dto.Coupon();
        coupon.setType(coupon1.getType());
        BxGyDetail bxGyDetail = new BxGyDetail();
        List<BuyProduct> buyProductList = coupon1.getDetails().getBuyProductList().stream()
                .map(buy -> new BuyProduct((long) buy.getProductId(), buy.getQuantity()))
                .collect(Collectors.toList());
        bxGyDetail.setBuyProducts(buyProductList);
        List<GetProduct> getProductList = coupon1.getDetails().getGetProductList().stream()
                .map(get -> new GetProduct((long) get.getProductId(), get.getQuantity()))
                .collect(Collectors.toList());
        bxGyDetail.setGetProducts(getProductList);
        bxGyDetail.setRepetitionLimit((long) coupon1.getDetails().getRepitionLimit());
        coupon.setDetails(bxGyDetail);
        log.info("Mapped BxGy coupon DTO: {}", coupon);
        return coupon;
    }
}