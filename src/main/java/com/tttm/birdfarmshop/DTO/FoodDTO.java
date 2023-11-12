package com.tttm.birdfarmshop.DTO;

import com.tttm.birdfarmshop.Enums.ProductStatus;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodDTO {
    private String productName;

    private Integer price;

    private String description;

    private String typeOfProduct;

    private List<String> images;

    private String feedback;

    private Integer rating;
    private Integer quantity;
}
