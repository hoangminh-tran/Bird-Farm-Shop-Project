package com.tttm.birdfarmshop.Utils.Response;

import com.tttm.birdfarmshop.Enums.BirdColor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BirdResponse {
    private String productID;

    private String productName;

    private Integer price;

    private String description;

    private String typeOfProduct;

    private List<String> images;

    private String feedback;

    private Integer rating;

    private Integer quantity;

    private Integer age;

    private Boolean gender;

    private Boolean fertility;

    private BirdColor birdColor;

    private Integer breedingTimes;

    private String typeOfBirdID;

    private Integer healthcareProfessionalID;

    private Integer ownerID;
}
