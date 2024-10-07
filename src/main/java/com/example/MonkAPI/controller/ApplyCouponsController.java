package com.example.MonkAPI.controller;

import com.example.MonkAPI.pojo.cart.CartRequest;
import com.example.MonkAPI.service.ValidatingCouponsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST controller for applying coupons to a cart.
 */
@RestController
@RequestMapping("/applicable-coupons")
@Slf4j
public class ApplyCouponsController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ValidatingCouponsService validatingCouponsService;

    /**
     * Applies coupons to the given cart.
     *
     * @param cart JSON representation of the cart.
     * @return ResponseEntity with the result of coupon validation.
     */
    @PostMapping
    public ResponseEntity<?> applyCoupons(@RequestBody String cart) {
        long startTime = System.currentTimeMillis();
        try {
            log.info("Received cart for coupon application: {}", cart);
            CartRequest cartRequest = objectMapper.readValue(cart, CartRequest.class);
            log.info("Parsed cart request: {}", cartRequest);
            return ResponseEntity.ok(validatingCouponsService.validateCoupon(cartRequest));
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
