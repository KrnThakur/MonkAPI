package com.example.MonkAPI.pojo.cart_wise;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Detail {
    @JsonProperty("threshold")
    int threshold;
    int discount;
}
