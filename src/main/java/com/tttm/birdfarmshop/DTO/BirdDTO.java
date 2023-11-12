package com.tttm.birdfarmshop.DTO;

import com.tttm.birdfarmshop.Enums.BirdColor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BirdDTO {
    private String productName;

    private Integer price;

    private String description;

    private String typeOfProduct;

    private List<String> images;

    private Integer ownerID;

    private String feedback;

    private Integer rating;

    private Integer quantity;

    private Integer age;

    private Boolean gender;

    private Boolean fertility;

    private Integer breedingTimes;

    private BirdColor color;

    private String typeOfBirdID;

    private Integer healthcareProfessionalID;
}
