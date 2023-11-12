package com.tttm.birdfarmshop.Service.Impl;

import com.tttm.birdfarmshop.Enums.ProductStatus;
import com.tttm.birdfarmshop.Models.*;
import com.tttm.birdfarmshop.Repository.*;
import com.tttm.birdfarmshop.Service.HealthcareProfessionalService;
import com.tttm.birdfarmshop.Utils.Response.BirdResponse;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HealthcareProfessionalServiceImpl implements HealthcareProfessionalService {
    private final BirdRepository birdRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final HealthcareProfessionalRepository healthcareProfessionalRepository;
    private final TypeOfBirdRepository typeOfBirdRepository;
    private final ImageRepository imageRepository;
    private static final Logger logger = LogManager.getLogger(HealthcareProfessionalServiceImpl.class);

    @Override
    public void createHealthcareProfessional(User user) {
        healthcareProfessionalRepository.save(new HealthcareProfessional(user.getUserID()));
        logger.info("Create new Healthcare Professional Successfully");
    }

    @Override
    public List<BirdResponse> viewAllBirdBelongToHealthCare(int healthcareID) {
        List<BirdResponse> list = new ArrayList<>();
        Optional<HealthcareProfessional> healthcareProfessional = healthcareProfessionalRepository.findById(healthcareID);
        Optional<User> userOptional = userRepository.findById(healthcareID);

        if(healthcareProfessional.isEmpty() || userOptional.isEmpty())
        {
            return list;
        }
        List<Bird> birds = birdRepository.getAllBirdBelongToHealthcare(healthcareID);
        birds.forEach(bird -> {
            Optional<Product> productOptional = productRepository.findById(bird.getBirdID());
            productOptional.ifPresent(product -> {
                BirdResponse birdResponse = mapperedToBirdRepsonse(product, bird);
                list.add(birdResponse);
            });
        });
        return list;
    }

    @Override
    public MessageResponse updateBirdStatusHealthcare(String birdID) {
        Optional<Bird> birdOptional = birdRepository.findById(birdID);
        Optional<Product> productOptional = productRepository.findById(birdID);
        if (birdOptional.isEmpty() || productOptional.isEmpty()) return new MessageResponse("Fail to Update");

        ProductStatus status = productOptional.map(Product::getProductStatus).orElse(null);
        if(status != null)
        {
            if(status.equals(ProductStatus.AVAILABLE))
            {
                productOptional.get().setProductStatus(ProductStatus.UNAVAILABLE);
            }
            else if(status.equals(ProductStatus.UNAVAILABLE))
            {
                productOptional.get().setProductStatus(ProductStatus.DELETED);
            }
            else if(status.equals(ProductStatus.DELETED))
            {
                productOptional.get().setProductStatus(ProductStatus.AVAILABLE);
            }
            productRepository.save(productOptional.get());
            return new MessageResponse("Update Successfully");
        }
        return new MessageResponse("Fail to Update");
    }

    private BirdResponse mapperedToBirdRepsonse(Product product, Bird bird) {
        return BirdResponse.builder()
                .productID(product.getProductID())
                .productName(product.getProductName())
                .price(product.getPrice())
                .description(product.getDescription())
                .typeOfProduct(product.getTypeOfProduct().toUpperCase())
                .images(
                        imageRepository.findImageByProductID(product.getProductID())
                                .stream()
                                .map(Image::getImageUrl)
                                .collect(Collectors.toList())
                )
                .feedback(product.getFeedback())
                .rating(product.getRating())
                .quantity(product.getQuantity())
                .age(bird.getAge())
                .gender(bird.getGender())
                .fertility(bird.getFertility())
                .birdColor(bird.getColor())
                .breedingTimes(bird.getBreedingTimes())
                .typeOfBirdID(bird.getTypeOfBird().getTypeID())
                .healthcareProfessionalID(bird.getHealthcareProfessional().getHealthcareID())
                .ownerID(bird.getOwnerID())
                .build();

    }
}
