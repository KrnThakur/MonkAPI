package com.example.MonkAPI.service;

import com.example.MonkAPI.dto.*;
import com.example.MonkAPI.pojo.applicable_coupons.ApplicableCoupons;
import com.example.MonkAPI.pojo.applicable_coupons.ValidCoupons;
import com.example.MonkAPI.pojo.cart.CartRequest;
import com.example.MonkAPI.pojo.cart.Item;
import com.example.MonkAPI.repository.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for validating coupons.
 */
@Service
@Slf4j
public class ValidatingCouponsService {

    @Autowired
    private CouponRepository couponRepository;

    /**
     * Validates the coupons for the given cart.
     *
     * @param cart the cart request containing items.
     * @return ApplicableCoupons containing the list of valid coupons.
     */
    public ApplicableCoupons validateCoupon(CartRequest cart) {
        log.info("Starting coupon validation");
        ApplicableCoupons applicableCoupons = new ApplicableCoupons();
        List<ValidCoupons> validCoupons = new ArrayList<>();

        validateCartWiseCoupons(cart, validCoupons);
        validateProductWiseCoupons(cart, validCoupons);
        validateBxGyCoupons(cart, validCoupons);

        applicableCoupons.setValidCouponsList(validCoupons);
        log.info("Applicable coupons: {}", applicableCoupons);

        return applicableCoupons;
    }

    private void validateCartWiseCoupons(CartRequest cart, List<ValidCoupons> validCoupons) {
        log.info("Checking CartWise coupons");
        Long total = cart.getCart().getItems().stream()
                .mapToLong(item -> item.getPrice() * item.getQuantity())
                .sum();
        log.info("Total cart value: {}", total);

        List<Coupon> couponsCartWise = couponRepository.findByType("cart-wise");
        log.info("Coupons retrieved from repository for cart-wise: {}", couponsCartWise);

        if (!ObjectUtils.isEmpty(couponsCartWise) && couponsCartWise.get(0).getDetails() instanceof CartWiseDetail) {
            CartWiseDetail detail = (CartWiseDetail) couponsCartWise.get(0).getDetails();
            if (total >= detail.getThreshold()) {
                ValidCoupons validCoupon = new ValidCoupons();
                validCoupon.setCoupon_id(couponsCartWise.get(0).getId());
                validCoupon.setType(couponsCartWise.get(0).getType());
                validCoupon.setDiscount((long) detail.getDiscount());
                validCoupons.add(validCoupon);
            }
        }
    }

    private void validateProductWiseCoupons(CartRequest cart, List<ValidCoupons> validCoupons) {
        log.info("Checking ProductWise coupons");
        List<Coupon> couponsProductWise = couponRepository.findByType("product-wise");
        log.info("Coupons retrieved from repository for product-wise: {}", couponsProductWise);

        if (!ObjectUtils.isEmpty(couponsProductWise)) {
            Map<Long, Long> productPriceMapFromDb = couponsProductWise.stream()
                    .collect(Collectors.toMap(
                            coupon -> ((ProductWiseDetail) coupon.getDetails()).getProductId(),
                            coupon -> ((ProductWiseDetail) coupon.getDetails()).getDiscount()
                    ));
            log.info("Product price map from db: {}", productPriceMapFromDb);

            List<Long> cartProductIds = cart.getCart().getItems().stream()
                    .map(Item::getProductId)
                    .collect(Collectors.toList());
            log.info("Product ids from cart: {}", cartProductIds);

            for (Long productId : cartProductIds) {
                if (productPriceMapFromDb.containsKey(productId)) {
                    ValidCoupons validCoupon = new ValidCoupons();
                    validCoupon.setCoupon_id(productId);
                    validCoupon.setType("product-wise");
                    validCoupon.setDiscount(productPriceMapFromDb.get(productId));
                    validCoupons.add(validCoupon);
                }
            }
        }
    }

    private void validateBxGyCoupons(CartRequest cart, List<ValidCoupons> validCoupons) {
        log.info("Checking BxGy coupons");
        List<Coupon> couponsBxGy = couponRepository.findByType("bxgy");

        for (Coupon coupon : couponsBxGy) {
            BxGyDetail bxGyDetail = (BxGyDetail) coupon.getDetails();
            List<BuyProduct> buyProductList = bxGyDetail.getBuyProducts();
            List<GetProduct> getProductList = bxGyDetail.getGetProducts();

            for (Item item : cart.getCart().getItems()) {
                for (BuyProduct buyProduct : buyProductList) {
                    if (item.getProductId().equals(buyProduct.getProductId()) && item.getQuantity() >= buyProduct.getQuantity()) {
                        Long repeat = item.getQuantity() / buyProduct.getQuantity();
                        Long repetitionLimit = validCoupons.stream()
                                .filter(validCoupon -> validCoupon.getCoupon_id().equals(coupon.getId()))
                                .count();

                        if (repetitionLimit < bxGyDetail.getRepetitionLimit()) {
                            for (GetProduct getProduct : getProductList) {
                                Item getItem = cart.getCart().getItems().stream()
                                        .filter(cartItem -> cartItem.getProductId().equals(getProduct.getProductId()))
                                        .findFirst()
                                        .orElse(null);

                                if (getItem != null) {
                                    Long freeQuantityRequest = getItem.getQuantity();
                                    while (repeat > 0 && repetitionLimit < bxGyDetail.getRepetitionLimit() && freeQuantityRequest >= getProduct.getQuantity()) {
                                        ValidCoupons validCoupon = new ValidCoupons();
                                        validCoupon.setCoupon_id(coupon.getId());
                                        validCoupon.setType("bxgy");
                                        validCoupon.setDiscount(getItem.getPrice());
                                        validCoupons.add(validCoupon);
                                        freeQuantityRequest -= getProduct.getQuantity();
                                        repetitionLimit++;
                                        repeat--;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}