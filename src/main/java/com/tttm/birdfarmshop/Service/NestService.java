package com.tttm.birdfarmshop.Service;

import com.tttm.birdfarmshop.DTO.NestDTO;
import com.tttm.birdfarmshop.Utils.Request.FilterFoodNest;
import com.tttm.birdfarmshop.Utils.Request.ProductRequest;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import com.tttm.birdfarmshop.Utils.Response.ProductResponse;

import java.util.List;

public interface NestService {
    MessageResponse AddNewNest(NestDTO dto);

    MessageResponse UpdateNest(String NestID, NestDTO dto);

    ProductResponse findNestByNestID(String NestID);

    List<ProductResponse> findAllNest();

    List<ProductResponse> findNestByPrice(FilterFoodNest filterFoodNest);

    List<ProductResponse> findNestByName(ProductRequest productRequest);
    List<ProductResponse> sortNestByPriceAscending();
    List<ProductResponse> sortNestByPriceDescending();
    List<ProductResponse> sortNestByProductNameAscending();
    List<ProductResponse> sortNestByProductNameDescending();
}
