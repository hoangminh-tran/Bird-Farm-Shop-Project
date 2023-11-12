package com.tttm.birdfarmshop.Service.Impl;


import com.tttm.birdfarmshop.Models.HealthcareProfessional;
import com.tttm.birdfarmshop.Models.Seller;
import com.tttm.birdfarmshop.Models.User;
import com.tttm.birdfarmshop.Repository.SellerRepository;
import com.tttm.birdfarmshop.Service.SellerService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private static final Logger logger = LogManager.getLogger(SellerServiceImpl.class);

    @Override
    public void createSeller(User user) {
        sellerRepository.save(new Seller(user.getUserID()));
        logger.info("Create new Seller Successfully");
    }
}
