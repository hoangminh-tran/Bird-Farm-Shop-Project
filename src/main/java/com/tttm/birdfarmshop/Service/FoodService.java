package com.tttm.birdfarmshop.Service;

import com.tttm.birdfarmshop.DTO.FoodDTO;
import com.tttm.birdfarmshop.Utils.Request.FilterFoodNest;
import com.tttm.birdfarmshop.Utils.Request.ProductRequest;
import com.tttm.birdfarmshop.Utils.Response.BirdResponse;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import com.tttm.birdfarmshop.Utils.Response.ProductResponse;

import java.util.List;

public interface FoodService {
    MessageResponse AddNewFood(FoodDTO dto);

    MessageResponse UpdateFood(String productID, FoodDTO dto);

    ProductResponse findFoodByFoodID(String foodID);

    List<ProductResponse> findAllFood();

    List<ProductResponse> findFoodByPrice(FilterFoodNest filterFoodNest);

    List<ProductResponse> findFoodByName(ProductRequest productRequest);

    List<ProductResponse> sortFoodByPriceAscending();
    List<ProductResponse> sortFoodByPriceDescending();

    List<ProductResponse> sortFoodByProductNameAscending();
    List<ProductResponse> sortFoodByProductNameDescending();
}
