package com.example.MonkAPI.service;

import com.example.MonkAPI.dto.BxGyDetail;
import com.example.MonkAPI.dto.Coupon;
import com.example.MonkAPI.repository.CouponRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PojoToDtoService pojoToDtoService;

    /**
     * Creates a new coupon.
     *
     * @param coupon the coupon to create.
     * @return the created coupon.
     */
    public Coupon createCoupon(Coupon coupon) {
        log.info("Creating new coupon: {}", coupon);
        if(coupon.getDetails() == null) {
            log.error("Coupon details cannot be null");
            throw new IllegalArgumentException("Coupon details cannot be null");
        }
        return couponRepository.save(coupon);
    }

    /**
     * Retrieves all coupons.
     *
     * @return a list of all coupons.
     */
    public List<Coupon> getAllCoupons() {
        log.info("Fetching all coupons");
        return couponRepository.findAll();
    }

    /**
     * Retrieves a coupon by its ID.
     *
     * @param id the ID of the coupon.
     * @return the coupon with the specified ID.
     * @throws RuntimeException if the coupon is not found.
     */
    public Coupon getCouponById(Long id) {
        log.info("Fetching coupon with ID: {}", id);
        return couponRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Coupon not found with ID: {}", id);
                    return new RuntimeException("Coupon not found");
                });
    }

    /**
     * Updates a coupon by its ID.
     *
     * @param id the ID of the coupon to update.
     * @param coupon the updated coupon details string.
     * @return the updated coupon.
     * @throws RuntimeException if the coupon is not found.
     */
    public Coupon updateCoupon(Long id, String coupon) throws JsonProcessingException {
        log.info("Updating coupon with ID: {}", id);
        if (coupon.contains("bxgy")) {
            com.example.MonkAPI.pojo.bxgy.Coupon bxgyCoupon = objectMapper.readValue(coupon, com.example.MonkAPI.pojo.bxgy.Coupon.class);
            Coupon coupon1 = pojoToDtoService.mapToBxGy(bxgyCoupon);
            log.info("Creating BxGy coupon: {}", bxgyCoupon);
            return couponRepository.findById(id)
                    .map(existingCoupon -> {
                        existingCoupon.setType(coupon1.getType());
                        existingCoupon.setDetails(coupon1.getDetails());
                        log.info("Updated coupon: {}", existingCoupon);
                        return couponRepository.save(existingCoupon);
                    })
                    .orElseThrow(() -> {
                        log.error("Coupon not found with ID: {}", id);
                        return new RuntimeException("Coupon not found");
                    });
        } else if (coupon.contains("cart-wise")) {
            com.example.MonkAPI.pojo.cart_wise.Coupon cartWiseCoupon = objectMapper.readValue(coupon, com.example.MonkAPI.pojo.cart_wise.Coupon.class);
            Coupon coupon1 = pojoToDtoService.mapToCartWise(cartWiseCoupon);
            log.info("Creating Cart-Wise coupon: {}", cartWiseCoupon);
            return couponRepository.findById(id)
                    .map(existingCoupon -> {
                        existingCoupon.setType(coupon1.getType());
                        existingCoupon.setDetails(coupon1.getDetails());
                        log.info("Updated coupon: {}", existingCoupon);
                        return couponRepository.save(existingCoupon);
                    })
                    .orElseThrow(() -> {
                        log.error("Coupon not found with ID: {}", id);
                        return new RuntimeException("Coupon not found");
                    });
        } else if(coupon.contains("product-wise")) {
            com.example.MonkAPI.pojo.productwise.Coupon productWiseCoupon = objectMapper.readValue(coupon, com.example.MonkAPI.pojo.productwise.Coupon.class);
            log.info("Creating Product-Wise coupon: {}", productWiseCoupon);
            Coupon coupon1 = pojoToDtoService.mapToProductWise(productWiseCoupon);
            return couponRepository.findById(id)
                    .map(existingCoupon -> {
                        existingCoupon.setType(coupon1.getType());
                        existingCoupon.setDetails(coupon1.getDetails());
                        log.info("Updated coupon: {}", existingCoupon);
                        return couponRepository.save(existingCoupon);
                    })
                    .orElseThrow(() -> {
                        log.error("Coupon not found with ID: {}", id);
                        return new RuntimeException("Coupon not found");
                    });
        }
        return null;
    }

    /**
     * Deletes a coupon by its ID.
     *
     * @param id the ID of the coupon to delete.
     * @throws RuntimeException if the coupon is not found.
     */
    public void deleteCoupon(Long id) {
        log.info("Deleting coupon with ID: {}", id);
        if (couponRepository.existsById(id)) {
            couponRepository.deleteById(id);
            log.info("Deleted coupon with ID: {}", id);
        } else {
            log.error("Coupon not found with ID: {}", id);
            throw new RuntimeException("Coupon not found");
        }
    }

}

