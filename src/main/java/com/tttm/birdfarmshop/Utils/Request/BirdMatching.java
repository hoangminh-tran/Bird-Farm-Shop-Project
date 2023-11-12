package com.tttm.birdfarmshop.Utils.Request;

import com.tttm.birdfarmshop.Enums.BirdColor;
import com.tttm.birdfarmshop.Models.Image;
import lombok.*;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BirdMatching {
    private String productName;
    private Integer price;
    private String description;
    private BirdColor birdColor;
    private List<String> images;
    private Integer age;
    private Boolean gender;
    private Boolean fertility;
    private Integer breedingTimes;
    private String typeOfBirdID;
}
