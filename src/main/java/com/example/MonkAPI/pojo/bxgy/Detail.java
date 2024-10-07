package com.example.MonkAPI.pojo.bxgy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Detail {
    @JsonProperty("buy_products")
    List<BuyProduct> buyProductList;
    @JsonProperty("get_products")
    List<GetProduct> getProductList;
    @JsonProperty("repition_limit")
    Long repitionLimit;
}
