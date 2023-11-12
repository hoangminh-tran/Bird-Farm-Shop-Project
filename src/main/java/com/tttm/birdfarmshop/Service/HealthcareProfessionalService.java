package com.tttm.birdfarmshop.Service;

import com.tttm.birdfarmshop.Models.User;
import com.tttm.birdfarmshop.Utils.Response.BirdResponse;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;

import java.util.List;

public interface HealthcareProfessionalService {

    void createHealthcareProfessional(User user);

    List<BirdResponse> viewAllBirdBelongToHealthCare(int healthcareID);

    MessageResponse updateBirdStatusHealthcare(String birdID);

}
