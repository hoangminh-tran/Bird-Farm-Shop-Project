package com.tttm.birdfarmshop.Utils.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterFoodNest {
    private Double left_range_price;

    private Double right_range_price;

    private String typeOfProduct;
}
