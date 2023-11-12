package com.tttm.birdfarmshop.Service.Impl;

import com.tttm.birdfarmshop.DTO.BirdDTO;
import com.tttm.birdfarmshop.Enums.BirdColor;
import com.tttm.birdfarmshop.Enums.BirdMatchingStatus;
import com.tttm.birdfarmshop.Enums.ProductStatus;
import com.tttm.birdfarmshop.Exception.CustomException;
import com.tttm.birdfarmshop.Models.*;
import com.tttm.birdfarmshop.Repository.*;
import com.tttm.birdfarmshop.Service.BirdService;
import com.tttm.birdfarmshop.Utils.Request.BirdMatching;
import com.tttm.birdfarmshop.Utils.Request.BirdRequest;
import com.tttm.birdfarmshop.Utils.Request.FilterProduct;
import com.tttm.birdfarmshop.Utils.Response.BirdMatchingResponse;
import com.tttm.birdfarmshop.Utils.Response.BirdResponse;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BirdServiceImpl implements BirdService {
    private final BirdRepository birdRepository;
    private final ProductRepository productRepository;
    private final HealthcareProfessionalRepository healthcareProfessionalRepository;
    private final TypeOfBirdRepository typeOfBirdRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final Logger logger = LoggerFactory.getLogger(BirdServiceImpl.class);

    private boolean isValidFood(BirdDTO dto) {
        return !dto.getProductName().isBlank() && !dto.getProductName().isEmpty() && dto.getPrice() >= 0
                && !dto.getTypeOfProduct().isEmpty() && !dto.getTypeOfProduct().isBlank() && dto.getRating() >= 0
                && !dto.getFertility().toString().isBlank() && !dto.getFertility().toString().isEmpty()
                && !dto.getTypeOfBirdID().isEmpty() && !dto.getTypeOfBirdID().isBlank() && dto.getOwnerID() >= 0;
    }

    @Override
    @CacheEvict(value = "birds", allEntries = true)
    public MessageResponse AddNewBird(BirdDTO dto) {
        Optional<TypeOfBird> typeOfBirdOptional = typeOfBirdRepository.findById(dto.getTypeOfBirdID());
        Optional<HealthcareProfessional> healthcareProfessionalOptional = healthcareProfessionalRepository.findById(dto.getHealthcareProfessionalID());

        if (typeOfBirdOptional.isEmpty() || healthcareProfessionalOptional.isEmpty() || !isValidFood(dto)) {
            return new MessageResponse("Fail");
        }

        int size = (int) birdRepository.count();
        String BirdID = "B00" + (size + 1);

        TypeOfBird typeOfBird = typeOfBirdOptional.get();
        HealthcareProfessional healthcareProfessional = healthcareProfessionalOptional.get();

        Product product = productRepository.save(
                Product.builder()
                        .productID(BirdID)
                        .productName(dto.getProductName())
                        .price(dto.getPrice())
                        .description(dto.getDescription())
                        .typeOfProduct(dto.getTypeOfProduct().toUpperCase())
                        .feedback(dto.getFeedback())
                        .rating(dto.getRating())
                        .productStatus(ProductStatus.AVAILABLE)
                        .quantity(dto.getQuantity())
                        .build()
        );

        List<String> listImages = dto.getImages();
        listImages.forEach(
                imageUrl -> imageRepository.save(
                        Image.builder()
                                .imageUrl(imageUrl)
                                .imageProduct(product)
                                .build())
        );

        birdRepository.save(
                Bird.builder()
                        .birdID(BirdID)
                        .age(dto.getAge())
                        .gender(dto.getGender())
                        .ownerID(dto.getOwnerID())
                        .fertility(dto.getFertility())
                        .breedingTimes(dto.getBreedingTimes())
                        .color(dto.getColor())
                        .typeOfBird(typeOfBird)
                        .healthcareProfessional(healthcareProfessional)
                        .build()
        );

        // Update Type Of Bird Quantity
        int quantity = typeOfBird.getQuantity();
        typeOfBird.setQuantity(quantity + 1);
        typeOfBirdRepository.save(typeOfBird);

        return new MessageResponse("Success");
    }


    @Transactional
    @Override
    @Caching(
            put = {
                    @CachePut(value = "bird", key = "#birdID", condition = "#birdID != null")
            },
            evict = {
                    @CacheEvict(value = "birds", allEntries = true)
            })
    public MessageResponse UpdateBird(String birdID, BirdDTO dto) {
        try {
            Optional<Product> productOptional = productRepository.findById(birdID);
            if (productOptional.isEmpty()) {
                return new MessageResponse("Fail");
            }

            Optional<TypeOfBird> typeOfBird = typeOfBirdRepository.findById(dto.getTypeOfBirdID());
            if (typeOfBird.isEmpty()) {
                return new MessageResponse("Fail");
            }

            Optional<HealthcareProfessional> healthcareProfessional = healthcareProfessionalRepository.findById(dto.getHealthcareProfessionalID());
            if (healthcareProfessional.isEmpty()) {
                return new MessageResponse("Fail");
            }

            if (isValidFood(dto)) {
                // Update Product Info
                Product product = productOptional.get();
                product.setProductName(dto.getProductName());
                product.setPrice(dto.getPrice());
                product.setDescription(dto.getDescription());
                product.setTypeOfProduct(dto.getTypeOfProduct().toUpperCase());
                product.setFeedback(dto.getFeedback());
                product.setRating(dto.getRating());
                product.setQuantity(dto.getQuantity());
                productRepository.save(product);

                List<String> listImages = dto.getImages();
                if (listImages.size() != 0) {
                    imageRepository.deleteImageByproductID(birdID);
                    listImages.forEach(
                            imageUrl -> imageRepository.save(
                                    Image.builder()
                                            .imageUrl(imageUrl)
                                            .imageProduct(product)
                                            .build())
                    );
                }


                // Update Bird Info
                Bird bird = birdRepository.findById(birdID).orElse(null);
                if (bird == null) {
                    return new MessageResponse("Fail");
                }

                TypeOfBird oldTypeOfBird = bird.getTypeOfBird();

                bird.setAge(dto.getAge());
                bird.setGender(dto.getGender());
                bird.setFertility(dto.getFertility());
                bird.setTypeOfBird(typeOfBird.get());
                bird.setBreedingTimes(dto.getBreedingTimes());
                bird.setHealthcareProfessional(healthcareProfessional.get());
                bird.setColor(dto.getColor());
                bird.setOwnerID(dto.getOwnerID());
                birdRepository.save(bird);

                // Update Type Of Bird Quantity
                int oldQuantity = oldTypeOfBird.getQuantity();
                oldTypeOfBird.setQuantity(oldQuantity - 1);
                typeOfBirdRepository.save(oldTypeOfBird);

                int newQuantity = typeOfBird.get().getQuantity();
                typeOfBird.get().setQuantity(newQuantity + 1);
                typeOfBirdRepository.save(typeOfBird.get());

                return new MessageResponse("Success");
            } else {
                return new MessageResponse("Fail");
            }
        } catch (Exception ex) {
            return new MessageResponse("Fail");
        }
    }

    @Override
    @Cacheable(value = "bird", key = "#birdID", condition = "#birdID != null")
    public BirdResponse findBirdByBirdID(String birdID) {
        try {
            Optional<Product> productOptional = productRepository.findById(birdID);
            Optional<Bird> birdOptional = birdRepository.findById(birdID);
            if (productOptional.isPresent() && birdOptional.isPresent()) {
                return mapperedToBirdRepsonse(productOptional.get(), birdOptional.get());
            } else return new BirdResponse();
        } catch (Exception e) {
            return new BirdResponse();
        }
    }

    @Override
    @Cacheable(value = "birds")
    public List<BirdResponse> findAllBird() {
        List<BirdResponse> BirdResponseList = new ArrayList<>();
        List<Product> productList = productRepository.findAllBird();
        for (Product product : productList) {
            Optional<Bird> birdOptional = birdRepository.findById(product.getProductID());
            if (birdOptional.isPresent() && product.getQuantity() > 0) {
                BirdResponseList.add(mapperedToBirdRepsonse(product, birdOptional.get()));
            }
        }
        return BirdResponseList;
    }

    @Override
    public BirdResponse updateBirdOwner(String birdID, int customerID) {
        Optional<User> userOptional = userRepository.findById(customerID);
        Optional<Customer> customerOptional = customerRepository.findById(customerID);
        Optional<Bird> birdOptional = birdRepository.findById(birdID);
        Optional<Product> productOptional = productRepository.findById(birdID);
        if(userOptional.isEmpty() || customerOptional.isEmpty() || birdOptional.isEmpty() || productOptional.isEmpty())
        {
            return new BirdResponse();
        }
        birdOptional.get().setOwnerID(customerID);
        birdRepository.save(birdOptional.get());
        return mapperedToBirdRepsonse(productOptional.get(), birdOptional.get());
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

    private boolean checkBirdInfo(BirdMatching bird) {
        if (bird.getBreedingTimes() >= 8) {
            return false;
        }
        if (bird.getAge() >= 8) {
            return false;
        }
        return true;
    }

    private float simulateMatching(long firstNum, long secondNum) {
        return (float) ((firstNum + secondNum) / 100) % 100;
    }

    private <T> long birdToNum(T bird) {
        try {
            // Tạo một đối tượng MessageDigest với thuật toán MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Băm chuỗi thành một mảng byte
            byte[] byteData = md.digest(bird.toString().getBytes());

            // Chuyển mảng byte thành một số dương
            long hashValue = bytesToLong(byteData) % 100000;
            return hashValue;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private long bytesToLong(byte[] bytes) {
        long result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result = (result << 8) | (bytes[i] & 0xFF);
        }
        return Math.abs(result); // Đảm bảo kết quả là số dương
    }

    private String getSizeOfBird(long num) {
        if (num <= 33333) {
            return "S";
        }
        if (num <= 66666) {
            return "M";
        }
        return "L";
    }

    private BirdColor caculateResult(BirdMatching firstBird, BirdMatching seconBird) {
        if (birdToNum(firstBird) >= birdToNum(seconBird)) {
            return firstBird.getBirdColor();
        } else {
            return seconBird.getBirdColor();
        }
    }

    @Override
    public BirdMatchingResponse matchingSameOwner(BirdMatching firstBird, BirdMatching secondBird) throws CustomException {
        if (!checkBirdInfo(firstBird)) {
            throw new CustomException("The Bird: " + firstBird.toString() + " is not eligible for pairing");
        }
        if (!checkBirdInfo(secondBird)) {
            throw new CustomException("The Bird: " + secondBird.toString() + " is not eligible for pairing");
        }

        if (!firstBird.getTypeOfBirdID().equals(secondBird.getTypeOfBirdID())) {
            throw new CustomException("This pare can not matching. Different type.");
        }

        TypeOfBird tb = typeOfBirdRepository.findById(firstBird.getTypeOfBirdID()).map(type -> {
                    return type;
                })
                .orElse(null);
        if(tb == null){
            throw new CustomException("Invalid Type of Bird");
        }

        if (firstBird.getGender() == secondBird.getGender()) {
            throw new CustomException("This pare can not matching. The same gender.");
        }
//        System.out.println(birdToNum(firstBird));
//        System.out.println(birdToNum(secondBird));
        float simulate = simulateMatching(birdToNum(firstBird), birdToNum(secondBird));
        if (simulate < 20f) {
            throw new CustomException("The success rate is not good: " + simulate);
        }
        Random random = new Random();
        Bird expectedResult = new Bird().builder()
                .breedingTimes(0)
                .typeOfBird(tb)
                .gender(random.nextBoolean())
                .age(0)
                .fertility(true)
                .ownerID(0)
                .color(caculateResult(firstBird, secondBird))
                .build();

        BirdMatchingResponse birdResponse = new BirdMatchingResponse(expectedResult, simulate);

        return birdResponse;
    }

    private List<BirdRequest> getBirdList() {
        List<Bird> productList = birdRepository.findAll();
        List<BirdRequest> birdRequestList = null;
        for (Bird bird : productList) {
            if (birdRequestList == null) {
                birdRequestList = new ArrayList<>();
            }
            birdRequestList.add(
                    BirdRequest.builder()
                            .birdName(bird.getProduct().getProductName())
                            .typeOfBird(bird.getTypeOfBird().toString())
                            .description(bird.getProduct().getDescription())
                            .age(bird.getAge())
                            .gender(bird.getGender())
                            .breedingTimes(bird.getBreedingTimes())
                            .color(bird.getColor())
                            .images(
                                    imageRepository.findImageByProductID(bird.getBirdID())
                                            .stream()
                                            .map(Image::getImageUrl)
                                            .collect(Collectors.toList())
                            )
                            .build());
        }
        return birdRequestList;
    }

    @Override
    public List<BirdResponse> matchingBirdDifferentOwner(BirdMatching firstBird) throws CustomException {
        if (!checkBirdInfo(firstBird)) {
            throw new CustomException("The Bird: " + firstBird.toString() + " is not eligible for pairing");
        }
        List<BirdResponse> birdlist = findAllBird();
        List<BirdResponse> responseList = null;
        TypeOfBird tb = typeOfBirdRepository.findById(firstBird.getTypeOfBirdID()).map(type -> {
                    return type;
                })
                .orElse(null);
        if(tb == null){
            throw new CustomException("Invalid Type of Bird");
        }

        for (BirdResponse secondBird : birdlist) {
//            if(!checkBirdInfo(secondBird)){
//                continue;
//            }
            if (!firstBird.getTypeOfBirdID().equals(secondBird.getTypeOfBirdID())) {
                continue;
            }
            if (firstBird.getGender() == secondBird.getGender()) {
                continue;
            }
            float simulate = simulateMatching(birdToNum(firstBird), birdToNum(secondBird));
            if (simulate < 20f) {
                continue;
            }
            if (!firstBird.getTypeOfBirdID().equals(secondBird.getTypeOfBirdID())) {
                throw new CustomException("This pare can not matching. Different type.");
            }
//            BirdMatching second = new BirdMatching()
//                    .builder()
//                    .birdColor(secondBird.getBirdColor())
//                    .age(secondBird.getAge())
//                    .breedingTimes(secondBird.getBreedingTimes())
//                    .typeOfBirdID(secondBird.getTypeOfBirdID())
//                    .productName(secondBird.getProductName())
//                    .price(secondBird.getPrice())
//                    .description(secondBird.getDescription())
//                    .fertility(secondBird.getFertility())
//                    .gender(secondBird.getGender())
//                    .images(secondBird.getImages())
//                    .build();
//            Random random = new Random();
//            BirdResponse expectedResult = new BirdResponse().builder()
//                    .breedingTimes(0)
//                    .typeOfBirdID(tb.getTypeID())
//                    .gender(random.nextBoolean())
//                    .age(0)
//                    .fertility(true)
//                    .birdColor(caculateResult(firstBird, second))
//                    .build();
            if (responseList == null) {
                responseList = new ArrayList<>();
            }
            responseList.add(secondBird);
        }
        return responseList;
    }

    @Override
    public List<BirdResponse> matchingBirdInShop(String id) {
        List<BirdResponse> responseList = null;
        List<BirdResponse> birdlist = findAllBird();
        BirdResponse firstBird = findBirdByBirdID(id);
        for (BirdResponse secondBird : birdlist) {
//            if(!checkBirdInfo(secondBird)){
//                continue;
//            }
            if (secondBird.getProductID().toUpperCase().equals(id.toUpperCase())) {
                continue;
            }
            if (firstBird.getGender().equals(secondBird.getGender())) {
                continue;
            }
            if (firstBird.getTypeOfBirdID() != secondBird.getTypeOfBirdID()) {
                continue;
            }
            float simulate = simulateMatching(birdToNum(firstBird), birdToNum(secondBird));
            if (simulate < 20f) {
                continue;
            }
            if (responseList == null) {
                responseList = new ArrayList<>();
            }
            responseList.add(secondBird);
        }
        return responseList;
    }

    @Override
    public List<BirdResponse> findBirdByName(String name) {
        return productRepository
                .findAll()
                .stream()
                .filter(bird -> bird.getProductName().contains(name))
                .map(product -> birdRepository.findById(product.getProductID())
                        .map(Bird -> mapperedToBirdRepsonse(product, Bird))
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<BirdResponse> sortBirdByPriceAscending() {
        List<Product> products = productRepository.sortProductByPriceAndTypeOfProductAscending("BIRD");
        return products
                .stream()
                .map(product -> birdRepository.findById(product.getProductID())
                        .map(bird -> mapperedToBirdRepsonse(product, bird))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<BirdResponse> sortBirdByPriceDescending() {
        List<Product> products = productRepository.sortProductByPriceAndTypeOfProductDescending("BIRD");
        return products
                .stream()
                .map(product -> birdRepository.findById(product.getProductID())
                        .map(bird -> mapperedToBirdRepsonse(product, bird))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<BirdResponse> sortBirdByProductNameAscending() {
        List<Product> products = productRepository.sortProductByProductNameAndTypeOfProductAscending("BIRD");
        return products
                .stream()
                .map(product -> birdRepository.findById(product.getProductID())
                        .map(bird -> mapperedToBirdRepsonse(product, bird))
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    @Override
    public List<BirdResponse> sortBirdByProductNameDescending() {
        List<Product> products = productRepository.sortProductByProductNameAndTypeOfProductDescending("BIRD");
        return products
                .stream()
                .map(product -> birdRepository.findById(product.getProductID())
                        .map(bird -> mapperedToBirdRepsonse(product, bird))
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Product> getListProductByAge(List<Product> products, int left_range_age, int right_range_age) {
        return products.stream()
                .filter(product -> birdRepository.findById(product.getProductID())
                        .map(Bird::getAge)
                        .filter(age -> age >= left_range_age && age <= right_range_age)
                        .isPresent()
                )
                .collect(Collectors.toList());
    }

    private List<Product> getListProductByGender(List<Product> products, boolean gender) {
        return products.stream()
                .filter(product -> birdRepository.findById(product.getProductID())
                        .map(Bird::getGender)
                        .filter(birdGender -> birdGender == gender)
                        .isPresent()
                )
                .collect(Collectors.toList());
    }

    private List<Product> getListProductByFertility(List<Product> products, boolean fertility) {
        return products.stream()
                .filter(product -> birdRepository.findById(product.getProductID())
                        .map(Bird::getFertility)
                        .filter(birdFertility -> birdFertility == fertility)
                        .isPresent()
                )
                .collect(Collectors.toList());
    }

    private List<Product> getListProductByTypeOfBirdName(List<Product> products, String TypeOfBirdName) {
        return products.stream()
                .filter(product -> birdRepository.findById(product.getProductID())
                        .map(bird -> typeOfBirdRepository.findTypeOfBirdByBirdId(bird.getBirdID()))
                        .filter(typeOfBirds -> typeOfBirds.stream()
                                .anyMatch(typeOfBird -> typeOfBird.getTypeName().equals(TypeOfBirdName)))
                        .isPresent())
                .collect(Collectors.toList());
    }

    private List<Product> getListProductByPrice(List<Product> products, Double leftRangePrice, Double rightRangePrice) {
        return products.stream()
                .filter(product -> birdRepository.findById(product.getProductID())
                        .map(bird -> product.getPrice() >= leftRangePrice && product.getPrice() <= rightRangePrice)
                        .orElse(false)
                )
                .collect(Collectors.toList());
    }

    private List<Product> getListProductByColor(List<Product> products, String color) {
        return products.stream()
                .filter(product -> birdRepository.findById(product.getProductID())
                        .map(Bird::getColor)
                        .filter(birdColor -> birdColor.equals(color.toUpperCase()))
                        .isPresent()
                )
                .collect(Collectors.toList());
    }


    @Override
    public List<BirdResponse> filterBirdByCustomerRequest(FilterProduct filterProduct) {
        List<Product> products = productRepository.findAll();
        logger.info("Inside Initial Filter Bird: " + products.size());

        if (!filterProduct.getLeft_range_age().isEmpty() && !filterProduct.getLeft_range_age().isBlank()
                && !filterProduct.getRight_range_age().isEmpty() && !filterProduct.getRight_range_age().isBlank()) {
            products = getListProductByAge(products, Integer.parseInt(filterProduct.getLeft_range_age()), Integer.parseInt(filterProduct.getRight_range_age()));
            logger.info("Inside Check Age Filter Bird: " + products.size());
        }

        if (!filterProduct.getGender().isEmpty() && !filterProduct.getGender().isBlank()) {
            products = getListProductByGender(products, Boolean.parseBoolean(filterProduct.getGender()));
            logger.info("Inside Check Gender Filter Bird: " + products.size());
        }

        if (!filterProduct.getFertility().isBlank() && !filterProduct.getFertility().isEmpty()) {
            products = getListProductByFertility(products, Boolean.parseBoolean(filterProduct.getFertility()));
            logger.info("Inside Check Fertility Filter Bird: " + products.size());
        }

        if (!filterProduct.getTypeOfBirdName().isEmpty() && !filterProduct.getTypeOfBirdName().isBlank()) {
            products = getListProductByTypeOfBirdName(products, filterProduct.getTypeOfBirdName());
            logger.info("Inside Check TypeOfBirdName Filter Bird: " + products.size());
        }

        if (!filterProduct.getLeft_range_price().isEmpty() && !filterProduct.getLeft_range_price().isBlank()
                && !filterProduct.getRight_range_price().isEmpty() && !filterProduct.getRight_range_price().isBlank()) {
            products = getListProductByPrice(products, Double.parseDouble(filterProduct.getLeft_range_price()), Double.parseDouble(filterProduct.getRight_range_price()));
            logger.info("Inside Check Price Filter Bird: " + products.size());
        }

        if (!filterProduct.getColor().isEmpty() && !filterProduct.getColor().isBlank()) {
            products = getListProductByColor(products, filterProduct.getColor());
            logger.info("Inside Check Color Filter Bird: " + products.size());
        }

        logger.info("After Check All Filter Request");
        return products
                .stream()
                .map(product -> birdRepository.findById(product.getProductID())
                        .map(bird -> mapperedToBirdRepsonse(product, bird))
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
