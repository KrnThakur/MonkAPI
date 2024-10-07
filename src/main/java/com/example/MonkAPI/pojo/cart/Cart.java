package com.example.MonkAPI.pojo.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.util.List;
@Data
public class Cart {
    @JsonProperty("items")
    private List<Item> items;
}
