package com.example.MonkAPI.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class BxGyDetail extends CouponDetail {

    @ElementCollection
    private List<BuyProduct> buyProducts;

    @ElementCollection
    private List<GetProduct> getProducts;

    private Long repetitionLimit;
}

