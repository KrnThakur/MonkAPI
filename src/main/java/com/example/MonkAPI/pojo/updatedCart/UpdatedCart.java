package com.example.MonkAPI.pojo.updatedCart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedCart {
    private List<Item> items;
    private Long totalPrice;
    private Long totalDiscount;
    private Long finalPrice;
}
