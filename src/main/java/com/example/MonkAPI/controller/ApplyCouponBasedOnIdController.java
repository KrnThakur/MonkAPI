package com.example.MonkAPI.controller;

import com.example.MonkAPI.pojo.cart.CartRequest;
import com.example.MonkAPI.service.ApplyCouponBasedOnIdService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for applying a coupon based on its ID.
 */
@RestController
@RequestMapping("/apply-coupon")
@Slf4j
public class ApplyCouponBasedOnIdController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplyCouponBasedOnIdService applyCouponBasedOnIdService;

    /**
     * Applies a coupon to the given cart based on the coupon ID.
     *
     * @param id the ID of the coupon to apply.
     * @param cart JSON representation of the cart.
     * @return ResponseEntity with the result of coupon application.
     */
    @PostMapping("/{id}")
    public ResponseEntity<?> applyCoupons(@PathVariable Long id, @RequestBody String cart) {
        long startTime = System.currentTimeMillis();
        try {
            log.info("Received request to apply coupon with ID: {} to cart: {}", id, cart);
            CartRequest cartRequest = objectMapper.readValue(cart, CartRequest.class);
            log.info("Parsed cart request: {}", cartRequest);
            return ResponseEntity.ok(applyCouponBasedOnIdService.applyCouponBasedOnId(cartRequest, id));
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Invalid JSON format");
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Internal server error");
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("Processing time for applyCoupons: {} ms", (endTime - startTime));
        }
    }
}