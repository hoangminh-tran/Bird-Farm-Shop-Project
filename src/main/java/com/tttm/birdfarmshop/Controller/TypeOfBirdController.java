package com.tttm.birdfarmshop.Controller;

import com.tttm.birdfarmshop.Constant.ConstantAPI;
import com.tttm.birdfarmshop.Constant.ConstantParametter;
import com.tttm.birdfarmshop.DTO.TypeOfBirdDTO;
import com.tttm.birdfarmshop.Exception.CustomException;
import com.tttm.birdfarmshop.Models.TypeOfBird;
import com.tttm.birdfarmshop.Service.TypeOfBirdService;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import com.tttm.birdfarmshop.Utils.Response.TypeOfBirdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ConstantAPI.TYPE_OF_BIRD)
public class TypeOfBirdController {
    private final TypeOfBirdService typeOfBirdService;
    @PostMapping(ConstantAPI.ADD_TYPE_OF_BIRD)
    public ResponseEntity<MessageResponse> addTypeOfBird(@RequestBody TypeOfBirdDTO dto) throws CustomException {
        try {
            return new ResponseEntity<>(typeOfBirdService.AddNewTypeOfBird(dto), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    @PutMapping(ConstantAPI.UPDATE_TYPE_OF_BIRD + ConstantParametter.TYPE_OF_BIRD_ID)
    public ResponseEntity<MessageResponse> updateTypeOfBird(@PathVariable ("TypeID") String TypeID,
                                                            @RequestBody TypeOfBirdDTO dto) throws CustomException {
        try {
            return new ResponseEntity<>(typeOfBirdService.UpdateTypeOfBird(TypeID, dto), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    @GetMapping(ConstantAPI.GET_TYPE_OF_BIRD_BY_ID + ConstantParametter.TYPE_OF_BIRD_ID)
    public ResponseEntity<TypeOfBirdResponse> getTypeOfBirdByID(@PathVariable ("TypeID") String TypeID) throws CustomException {
        try {
            return new ResponseEntity<>(typeOfBirdService.findTypeOfBirdByID(TypeID), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ConstantAPI.GET_ALL_TYPE_OF_BIRD)
    public ResponseEntity<List<TypeOfBirdResponse>> getAllTypeOfBird() throws CustomException {
        try {
            return new ResponseEntity<>(typeOfBirdService.findAllTypeOfBird(), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ConstantAPI.GET_TYPE_OF_BIRD_BY_NAME + ConstantParametter.TYPE_OF_BIRD_NAME)
    public ResponseEntity<List<TypeOfBirdResponse>> getTypeOfBirdByName(@PathVariable("TypeOfBirdName") String TypeOfBirdName) throws CustomException {
        try {
            return new ResponseEntity<>(typeOfBirdService.findTypeOfBirdByName(TypeOfBirdName), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

}
