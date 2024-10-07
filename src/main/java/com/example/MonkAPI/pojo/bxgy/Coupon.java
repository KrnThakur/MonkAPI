package com.example.MonkAPI.pojo.bxgy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Coupon {
    String type;
    @JsonProperty("details")
    Detail details;





}
