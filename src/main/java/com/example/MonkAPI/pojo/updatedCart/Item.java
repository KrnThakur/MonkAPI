package com.example.MonkAPI.pojo.updatedCart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Long productId;
    private Long quantity;
    private Long price;
    private Long totalDiscount;
}
