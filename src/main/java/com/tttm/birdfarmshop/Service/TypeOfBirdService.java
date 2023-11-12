package com.tttm.birdfarmshop.Service;

import com.tttm.birdfarmshop.DTO.TypeOfBirdDTO;
import com.tttm.birdfarmshop.Models.TypeOfBird;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import com.tttm.birdfarmshop.Utils.Response.ProductResponse;
import com.tttm.birdfarmshop.Utils.Response.TypeOfBirdResponse;

import java.util.Enumeration;
import java.util.List;

public interface TypeOfBirdService {
    MessageResponse AddNewTypeOfBird(TypeOfBirdDTO dto);

    MessageResponse UpdateTypeOfBird(String typeID, TypeOfBirdDTO dto);

    TypeOfBirdResponse findTypeOfBirdByID(String typeID);

    List<TypeOfBirdResponse> findAllTypeOfBird();

    List<TypeOfBirdResponse> findTypeOfBirdByName(String name);
}
