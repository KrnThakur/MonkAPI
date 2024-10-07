package com.example.MonkAPI.pojo.productwise;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Detail {
    @JsonProperty("product_id")
    Long productId;
    Long discount;
}
