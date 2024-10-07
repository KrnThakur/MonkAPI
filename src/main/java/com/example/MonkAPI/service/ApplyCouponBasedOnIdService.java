package com.example.MonkAPI.service;

import com.example.MonkAPI.dto.*;
import com.example.MonkAPI.pojo.cart.CartRequest;
import com.example.MonkAPI.pojo.cart.Item;
import com.example.MonkAPI.pojo.updatedCart.UpdatedCart;
import com.example.MonkAPI.repository.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApplyCouponBasedOnIdService {


    @Autowired
    private CouponRepository couponRepository;

    /**
     * Applies a coupon to the given cart based on the coupon ID.
     *
     * @param cart the cart request containing items.
     * @param id   the ID of the coupon to apply.
     * @return UpdatedCart containing the updated cart details.
     */
    public UpdatedCart applyCouponBasedOnId(CartRequest cart, Long id) {
        log.info("Applying coupon with ID: {} to cart: {}", id, cart);
        Optional<Coupon> couponOptional = couponRepository.findById(id);

        if (couponOptional.isPresent()) {
            Coupon coupon = couponOptional.get();
            Long total = calculateTotalCartValue(cart);

            switch (coupon.getType()) {
                case "cart-wise":
                    return applyCartWiseCoupon(cart, coupon, total);
                case "product-wise":
                    return applyProductWiseCoupon(cart, coupon, total);
                case "bxgy":
                    return applyBxGyCoupon(cart, coupon);
                default:
                    log.error("Unknown coupon type: {}", coupon.getType());
                    throw new IllegalArgumentException("Unknown coupon type");
            }
        } else {
            log.error("Coupon not found with ID: {}", id);
            throw new RuntimeException("Coupon not found");
        }
    }

    private Long calculateTotalCartValue(CartRequest cart) {
        return cart.getCart().getItems().stream()
                .mapToLong(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    private UpdatedCart applyCartWiseCoupon(CartRequest cart, Coupon coupon, Long total) {
        CartWiseDetail detail = (CartWiseDetail) coupon.getDetails();
        UpdatedCart updatedCart = initializeUpdatedCart(cart, total);

        if (total >= detail.getThreshold()) {
            updatedCart.setTotalDiscount((long) detail.getDiscount());
            updatedCart.setFinalPrice(total - detail.getDiscount());
        } else {
            updatedCart.setTotalDiscount(0L);
            updatedCart.setFinalPrice(total);
        }

        log.info("Applied cart-wise coupon: {}", updatedCart);
        return updatedCart;
    }

    private UpdatedCart applyProductWiseCoupon(CartRequest cart, Coupon coupon, Long total) {
        ProductWiseDetail detail = (ProductWiseDetail) coupon.getDetails();
        UpdatedCart updatedCart = initializeUpdatedCart(cart, total);

        for (com.example.MonkAPI.pojo.updatedCart.Item updatedItem : updatedCart.getItems()) {
            if (updatedItem.getProductId().equals(detail.getProductId())) {
                long discount = detail.getDiscount() * updatedItem.getQuantity();
                updatedItem.setTotalDiscount(discount);
                updatedCart.setTotalDiscount(discount);
                updatedCart.setFinalPrice(total - discount);
            }
        }

        log.info("Applied product-wise coupon: {}", updatedCart);
        return updatedCart;
    }


    private UpdatedCart applyBxGyCoupon(CartRequest cart, Coupon coupon) {
        BxGyDetail detail = (BxGyDetail) coupon.getDetails();
        UpdatedCart updatedCart = new UpdatedCart();
        List<com.example.MonkAPI.pojo.updatedCart.Item> updatedItems = new ArrayList<>();
        List<Long> requestProductId = cart.getCart().getItems().stream().map(Item::getProductId).collect(Collectors.toList());
        Map<Long, Long> getProductIdQuantity = detail.getGetProducts().stream().collect(Collectors.toMap(GetProduct::getProductId, GetProduct::getQuantity));
        Map<Long, Long> buyProductIdQuantity = detail.getBuyProducts().stream().collect(Collectors.toMap(BuyProduct::getProductId, BuyProduct::getQuantity));
        Boolean flag = buyProductIdQuantity.containsKey(requestProductId);
        Long repeatLimit = 0L;
        Long totalPrice = 0L;
        Long totalDiscount = 0L;


        for (Item item : cart.getCart().getItems()) {
            if (buyProductIdQuantity.containsKey(item.getProductId())) {
                com.example.MonkAPI.pojo.updatedCart.Item updatedCartItem = new com.example.MonkAPI.pojo.updatedCart.Item();
                updatedCartItem.setProductId(item.getProductId());
                updatedCartItem.setQuantity(item.getQuantity());
                updatedCartItem.setPrice(item.getPrice());
                updatedCartItem.setTotalDiscount(0l);
                updatedItems.add(updatedCartItem);
                totalPrice += item.getPrice() * item.getQuantity();
                if (item.getQuantity() >= buyProductIdQuantity.get(item.getProductId())) {

                    int allowedQuantity = (int) (item.getQuantity() / buyProductIdQuantity.get(item.getProductId()));
                    for (Item requestGetProduct : cart.getCart().getItems()) {
                        for (GetProduct getProduct : detail.getGetProducts()) {
                            if (requestGetProduct.getProductId().equals(getProduct.getProductId()) && requestGetProduct.getQuantity() >= getProduct.getQuantity()) {
                                if (repeatLimit <= detail.getRepetitionLimit()) {
                                    repeatLimit = (long) allowedQuantity;
                                    com.example.MonkAPI.pojo.updatedCart.Item update = new com.example.MonkAPI.pojo.updatedCart.Item();
                                    update.setProductId(requestGetProduct.getProductId());

                                    update.setQuantity(allowedQuantity <= detail.getRepetitionLimit() ? requestGetProduct.getQuantity() + allowedQuantity : requestGetProduct.getQuantity() + detail.getRepetitionLimit());
                                    update.setPrice(requestGetProduct.getPrice());

                                    update.setTotalDiscount(allowedQuantity <= detail.getRepetitionLimit() ? requestGetProduct.getPrice() * allowedQuantity : requestGetProduct.getPrice() * detail.getRepetitionLimit());
                                    totalDiscount += update.getTotalDiscount();
                                    totalPrice += update.getQuantity() * update.getPrice();
                                    updatedItems.add(update);
                                }

                            }
                        }
                    }

                }


            } else {
                if (updatedItems.stream().noneMatch(updatedItem -> updatedItem.getProductId().equals(item.getProductId()))) {
                    com.example.MonkAPI.pojo.updatedCart.Item updatedCartItem = new com.example.MonkAPI.pojo.updatedCart.Item();
                    updatedCartItem.setProductId(item.getProductId());
                    updatedCartItem.setQuantity(item.getQuantity());
                    updatedCartItem.setPrice(item.getPrice());
                    updatedCartItem.setTotalDiscount(0l);
                    updatedItems.add(updatedCartItem);
                }
            }

        }
        updatedCart.setItems(updatedItems);
        updatedCart.setTotalPrice(totalPrice);
        updatedCart.setTotalDiscount(totalDiscount);
        updatedCart.setFinalPrice(totalPrice - totalDiscount);
        log.info("Applied BxGy coupon: {}", updatedCart);
        return updatedCart;
    }

    private UpdatedCart initializeUpdatedCart(CartRequest cart, Long total) {
        UpdatedCart updatedCart = new UpdatedCart();
        List<com.example.MonkAPI.pojo.updatedCart.Item> updatedItems = new ArrayList<>();

        for (Item item : cart.getCart().getItems()) {
            com.example.MonkAPI.pojo.updatedCart.Item updatedCartItem = new com.example.MonkAPI.pojo.updatedCart.Item();
            updatedCartItem.setProductId(item.getProductId());
            updatedCartItem.setQuantity(item.getQuantity());
            updatedCartItem.setPrice(item.getPrice());
            updatedCartItem.setTotalDiscount(0L);
            updatedItems.add(updatedCartItem);
        }

        updatedCart.setItems(updatedItems);
        updatedCart.setTotalPrice(total);
        updatedCart.setTotalDiscount(0L);
        updatedCart.setFinalPrice(total);

        return updatedCart;
    }


}



