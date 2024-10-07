package com.example.MonkAPI.pojo.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Item {
    @JsonProperty("product_id")
    private Long productId;
    @JsonProperty("quantity")
    private Long quantity;
    @JsonProperty("price")
    private Long price;
}
