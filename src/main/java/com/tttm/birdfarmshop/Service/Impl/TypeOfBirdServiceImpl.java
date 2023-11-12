package com.tttm.birdfarmshop.Service.Impl;

import com.tttm.birdfarmshop.DTO.TypeOfBirdDTO;
import com.tttm.birdfarmshop.Models.Product;
import com.tttm.birdfarmshop.Models.TypeOfBird;
import com.tttm.birdfarmshop.Repository.TypeOfBirdRepository;
import com.tttm.birdfarmshop.Service.TypeOfBirdService;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import com.tttm.birdfarmshop.Utils.Response.TypeOfBirdResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TypeOfBirdServiceImpl implements TypeOfBirdService {

    private final TypeOfBirdRepository typeOfBirdRepository;
    private final Logger logger = LoggerFactory.getLogger(TypeOfBirdServiceImpl.class);
    private boolean isValidTypeOfBird(TypeOfBirdDTO dto) {
        return !dto.getTypeName().isBlank()
                && !dto.getTypeName().isEmpty()
                && dto.getQuantity() >= 0;
    }

    @Override
    @CacheEvict(value = "typeOfBirds", allEntries = true)
    public MessageResponse AddNewTypeOfBird(TypeOfBirdDTO dto) {
        int size = (int) typeOfBirdRepository.findAll().stream().count();
        String typeID = "TB00" + (size + 1);

        if(isValidTypeOfBird(dto)) {
            typeOfBirdRepository.save(
                    TypeOfBird
                            .builder()
                            .typeID(typeID)
                            .typeName(dto.getTypeName())
                            .quantity(0)
                            .build());
            return new MessageResponse("Success");
        }
        return new MessageResponse("Fail");
    }

    @Override
    @Caching(
            put = {
                    @CachePut(value = "typeOfBird", key = "#typeID", condition = "#typeID != null")
            },
            evict = {
                    @CacheEvict(value = "typeOfBirds", allEntries = true)
            })
    public MessageResponse UpdateTypeOfBird(String typeID, TypeOfBirdDTO dto) {
       try
       {
           Optional<TypeOfBird> TypeOfBirdOptional = typeOfBirdRepository.findById(typeID);
           if(TypeOfBirdOptional.isEmpty() || !isValidTypeOfBird(dto))
           {
               return new MessageResponse("Fail");
           }

           return new MessageResponse(
                   Optional
                           .ofNullable(typeOfBirdRepository.findById(typeID).get())
                           .map(typeOfBird -> {
                               typeOfBird.setTypeName(dto.getTypeName());
                               typeOfBird.setQuantity(dto.getQuantity());
                               typeOfBirdRepository.save(typeOfBird);
                               return "Success";
                           })
                           .orElse("Fail"));
       }
       catch (Exception exception)
       {
           return new MessageResponse("Fail");
       }
    }

    @Override
    @Cacheable(value = "typeOfBird", key = "#typeID", condition = "#typeID != null")
    public TypeOfBirdResponse findTypeOfBirdByID(String typeID) {
        try{
            Optional<TypeOfBird> TypeOfBirdOptional = typeOfBirdRepository.findById(typeID);
            if(TypeOfBirdOptional.isPresent())
            {
                return mapperedToTypeOfBirdResponse(TypeOfBirdOptional.get());
            }
            else return new TypeOfBirdResponse();
        }
        catch (Exception e)
        {
            return new TypeOfBirdResponse();
        }
    }

    @Override
    @Cacheable(value = "typeOfBirds")
    public List<TypeOfBirdResponse> findAllTypeOfBird() {
        List<TypeOfBirdResponse> typeOfBirdResponseList = new ArrayList<>();
        List<TypeOfBird> list = typeOfBirdRepository.findAll();
        for(TypeOfBird typeOfBird : list)
        {
            typeOfBirdResponseList.add(mapperedToTypeOfBirdResponse(typeOfBird));
        }
        return typeOfBirdResponseList;
    }

    private TypeOfBirdResponse mapperedToTypeOfBirdResponse(TypeOfBird typeOfBird)
    {
        return TypeOfBirdResponse
                .builder()
                .typeID(typeOfBird.getTypeID())
                .typeName(typeOfBird.getTypeName())
                .quantity(typeOfBird.getQuantity())
                .build();
    }

    @Override
    @Cacheable(value = "typeOfBirds", key = "#name", condition = "#name != null")
    public List<TypeOfBirdResponse> findTypeOfBirdByName(String name) {
        return typeOfBirdRepository.findAll()
                .stream()
                .filter(typeOfBird -> typeOfBird.getTypeName().contains(name))
                .map(this::mapperedToTypeOfBirdResponse)
                .collect(Collectors.toList());
    }
}
