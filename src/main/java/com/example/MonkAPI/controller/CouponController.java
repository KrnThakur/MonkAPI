package com.example.MonkAPI.controller;

import com.example.MonkAPI.dto.Coupon;
import com.example.MonkAPI.service.CouponService;
import com.example.MonkAPI.service.PojoToDtoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
@Slf4j
public class CouponController {
    @Autowired
    private CouponService couponService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PojoToDtoService pojoToDtoService;

    /**
     * Creates a new coupon.
     *
     * @param coupon JSON representation of the coupon.
     * @return ResponseEntity with the created coupon.
     */
    @PostMapping
    public ResponseEntity<Coupon> createCoupon(@RequestBody String coupon) {
        long startTime = System.currentTimeMillis();
        try {
            if (coupon.contains("bxgy")) {
                com.example.MonkAPI.pojo.bxgy.Coupon bxgyCoupon = objectMapper.readValue(coupon, com.example.MonkAPI.pojo.bxgy.Coupon.class);
                log.info("Creating BxGy coupon: {}", bxgyCoupon);
                return ResponseEntity.ok(couponService.createCoupon(pojoToDtoService.mapToBxGy(bxgyCoupon)));
            } else if (coupon.contains("cart-wise")) {
                com.example.MonkAPI.pojo.cart_wise.Coupon cartWiseCoupon = objectMapper.readValue(coupon, com.example.MonkAPI.pojo.cart_wise.Coupon.class);
                log.info("Creating Cart-Wise coupon: {}", cartWiseCoupon);
                return ResponseEntity.ok(couponService.createCoupon(pojoToDtoService.mapToCartWise(cartWiseCoupon)));
            } else if(coupon.contains("product-wise")) {
                com.example.MonkAPI.pojo.productwise.Coupon productWiseCoupon = objectMapper.readValue(coupon, com.example.MonkAPI.pojo.productwise.Coupon.class);
                log.info("Creating Product-Wise coupon: {}", productWiseCoupon);
                return ResponseEntity.ok(couponService.createCoupon(pojoToDtoService.mapToProductWise(productWiseCoupon)));
            }else {
                log.error("Invalid coupon type");
                return ResponseEntity.badRequest().build();
            }
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("Processing time for createCoupon: {} ms", (endTime - startTime));
        }
    }

    /**
     * Retrieves all coupons.
     *
     * @return ResponseEntity with the list of all coupons.
     */
    @GetMapping
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        long startTime = System.currentTimeMillis();
        try {
            log.info("Fetching all coupons");
            return ResponseEntity.ok(couponService.getAllCoupons());
        } catch (Exception e) {
            log.error("Error fetching coupons: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("Processing time for getAllCoupons: {} ms", (endTime - startTime));
        }
    }

    /**
     * Retrieves a coupon by its ID.
     *
     * @param id the ID of the coupon.
     * @return ResponseEntity with the coupon.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        try {
            log.info("Fetching coupon with ID: {}", id);
            return ResponseEntity.ok(couponService.getCouponById(id));
        } catch (Exception e) {
            log.error("Error fetching coupon with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("Processing time for getCouponById: {} ms", (endTime - startTime));
        }
    }

    /**
     * Updates a coupon by its ID.
     *
     * @param id the ID of the coupon.
     * @param coupon the updated coupon.
     * @return ResponseEntity with the updated coupon.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable Long id, @RequestBody String coupon) {
        long startTime = System.currentTimeMillis();
        try {
            log.info("Updating coupon with ID: {}", id);
            return ResponseEntity.ok(couponService.updateCoupon(id, coupon));
        } catch (Exception e) {
            log.error("Error updating coupon with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("Processing time for updateCoupon: {} ms", (endTime - startTime));
        }
    }

    /**
     * Deletes a coupon by its ID.
     *
     * @param id the ID of the coupon.
     * @return ResponseEntity with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        try {
            log.info("Deleting coupon with ID: {}", id);
            couponService.deleteCoupon(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting coupon with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("Processing time for deleteCoupon: {} ms", (endTime - startTime));
        }
    }
}

