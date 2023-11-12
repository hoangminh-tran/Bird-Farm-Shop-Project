package com.tttm.birdfarmshop.Controller;

import com.tttm.birdfarmshop.Constant.ConstantAPI;
import com.tttm.birdfarmshop.Constant.ConstantParametter;
import com.tttm.birdfarmshop.Service.HealthcareProfessionalService;
import com.tttm.birdfarmshop.Utils.Response.BirdResponse;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ConstantAPI.HEALTH_CARE_PROFESSIONAL)
public class HealthcareController {
    private final HealthcareProfessionalService healthcareProfessionalService;

    @GetMapping(ConstantAPI.VIEW_ALL_BIRD_BELONG_TO_HEALTHCARE + ConstantParametter.HEALTH_CARE_ID)
    public ResponseEntity<List<BirdResponse>> viewAllBirdBelongToHealthCare(@PathVariable("healthcareID") Integer healthcareID) throws com.tttm.birdfarmshop.Exception.CustomException {
        try {
            return new ResponseEntity<>(healthcareProfessionalService.viewAllBirdBelongToHealthCare(healthcareID), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(ConstantAPI.UPDATE_BIRD_STATUS_HEALTHCARE + ConstantParametter.BIRD_ID)
    public ResponseEntity<MessageResponse> updateBirdStatusHealthcare(@PathVariable("BirdID") String BirdID) throws com.tttm.birdfarmshop.Exception.CustomException {
        try {
            return new ResponseEntity<>(healthcareProfessionalService.updateBirdStatusHealthcare(BirdID), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
