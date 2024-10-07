package com.example.MonkAPI.pojo.bxgy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetProduct {
    @JsonProperty("product_id")
    Long productId;
    Long quantity;
}
