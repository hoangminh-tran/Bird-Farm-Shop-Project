package com.tttm.birdfarmshop.Utils.Request;

import com.tttm.birdfarmshop.Enums.BirdColor;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BirdRequest {
    private String birdName;
    private String typeOfBird;
    private String description;
    private Integer age;
    private Boolean gender;
    private Integer breedingTimes;
    private BirdColor color;
    private List<String> images;

    @Override
    public String toString() {
        return "Bird{" +
                "birdName='" + birdName + '\'' +
                ", typeOfBird='" + typeOfBird + '\'' +
                ", description='" + description + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", breedingTimes=" + breedingTimes +
                ", color=" + color +
                '}';
    }
}
