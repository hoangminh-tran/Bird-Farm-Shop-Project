package com.tttm.birdfarmshop.Utils.Request;

import jakarta.annotation.security.DenyAll;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterProduct {
    private String left_range_age;

    private String right_range_age;

    private String gender;

    private String fertility;

    private String typeOfBirdName;

    private String left_range_price;

    private String right_range_price;

    private String color;
}
