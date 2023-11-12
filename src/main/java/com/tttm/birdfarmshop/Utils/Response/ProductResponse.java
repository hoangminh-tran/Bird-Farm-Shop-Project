package com.tttm.birdfarmshop.Utils.Response;

import com.tttm.birdfarmshop.Enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private String productID;

    private String productName;

    private Integer price;

    private String description;

    private String typeOfProduct;

    private List<String> images;

    private String feedback;

    private ProductStatus productStatus;

    private Integer rating;

    private Integer quantity;
}
