package com.example.MonkAPI.pojo.dataFromDb;

import com.example.MonkAPI.dto.GetProduct;
import com.example.MonkAPI.pojo.bxgy.BuyProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultBxGy {
    BuyProduct buyProduct;
    List<GetProduct> getProducts;
    Long repetitionLimit;


}
