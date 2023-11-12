package com.tttm.birdfarmshop.Service.Impl;

import com.tttm.birdfarmshop.DTO.FoodDTO;
import com.tttm.birdfarmshop.Enums.ProductStatus;
import com.tttm.birdfarmshop.Models.Food;
import com.tttm.birdfarmshop.Models.Image;
import com.tttm.birdfarmshop.Models.Product;
import com.tttm.birdfarmshop.Repository.FoodRepository;
import com.tttm.birdfarmshop.Repository.ImageRepository;
import com.tttm.birdfarmshop.Repository.ProductRepository;
import com.tttm.birdfarmshop.Service.FoodService;
import com.tttm.birdfarmshop.Utils.Request.FilterFoodNest;
import com.tttm.birdfarmshop.Utils.Request.ProductRequest;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import com.tttm.birdfarmshop.Utils.Response.ProductResponse;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {
    private final FoodRepository foodRepository;
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final Logger logger = LoggerFactory.getLogger(FoodServiceImpl.class);
    private boolean isValidFood(FoodDTO dto)
    {
        return !dto.getProductName().isBlank() && !dto.getProductName().isEmpty() && dto.getPrice() >= 0
                && !dto.getTypeOfProduct().isEmpty() && !dto.getTypeOfProduct().isBlank() && dto.getRating() >= 0;
    }
    @Override
    @CacheEvict(value = "foods", allEntries = true)
    public MessageResponse AddNewFood(FoodDTO dto) {

        int size = (int) foodRepository.findAll().stream().count();
        String FoodID = "F00" + (size + 1);

        if(isValidFood(dto))
        {
            Product product = productRepository.save(Product.builder()
                    .productID(FoodID)
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
            foodRepository.save(new Food(FoodID));

            List<String> listImages = dto.getImages();
            listImages.forEach(
                    imageUrl -> imageRepository.save(
                            Image.builder()
                                    .imageUrl(imageUrl)
                                    .imageProduct(product)
                                    .build())
            );

            return new MessageResponse("Success");
        }
        return new MessageResponse("Fail");
    }

    @Transactional
    @Override
    @Caching(
            put = {
                    @CachePut(value = "food", key = "#foodID", condition = "#foodID != null")
            },
            evict = {
                    @CacheEvict(value = "foods", allEntries = true)
            })
    public MessageResponse UpdateFood(String foodID, FoodDTO dto) {
        try {
            Optional<Product> productOptional = productRepository.findById(foodID);
            if(productOptional.isEmpty() || !isValidFood(dto))
            {
                return new MessageResponse("Fail");
            }
            return new MessageResponse(
                    Optional
                            .ofNullable(productRepository.findById(foodID).get())
                            .map(food ->{
                                food.setProductName(dto.getProductName());
                                food.setPrice(dto.getPrice());
                                food.setDescription(dto.getDescription());
                                food.setTypeOfProduct(dto.getTypeOfProduct().toUpperCase());
                                food.setFeedback(dto.getFeedback());
                                food.setRating(dto.getRating());
                                food.setQuantity(dto.getQuantity());
                                productRepository.save(food);

                                List<String> listImages = dto.getImages();
                                if(listImages.size() != 0)
                                {
                                    imageRepository.deleteImageByproductID(foodID);
                                    listImages.forEach(
                                            imageUrl -> imageRepository.save(
                                                    Image.builder()
                                                            .imageUrl(imageUrl)
                                                            .imageProduct(food)
                                                            .build())
                                    );
                                }

                                return "Success";
                            })
                            .orElse("Fail")
                );
        }
        catch (Exception ex)
        {
            return new MessageResponse("Fail");
        }
    }

    private ProductResponse mapperedToProductResponse(Product product)
    {
        return ProductResponse.builder()
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
                .productStatus(product.getProductStatus())
                .rating(product.getRating())
                .quantity(product.getQuantity())
                .build();
    }

    @Override
    @Cacheable(value = "food", key = "#foodID", condition = "#foodID != null")
    public ProductResponse findFoodByFoodID(String foodID) {
        try
        {
            Optional<Product> foodOptional = productRepository.findById(foodID);
            if(foodOptional.isPresent())
            {
                return mapperedToProductResponse(foodOptional.get());
            }
            else return new ProductResponse();
        }
        catch (Exception e)
        {
            return new ProductResponse();
        }
    }

    @Override
    @Cacheable(value = "foods")
    public List<ProductResponse> findAllFood() {
        return productRepository.findAllFood()
                .stream()
                .map(this::mapperedToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> findFoodByPrice(FilterFoodNest filterFoodNest) {
        if(filterFoodNest.getLeft_range_price() > filterFoodNest.getRight_range_price()) return null;

        return productRepository.findAll()
                .stream()
                .filter(product -> product.getPrice() <= filterFoodNest.getRight_range_price() && product.getPrice() >= filterFoodNest.getLeft_range_price()
                        && product.getTypeOfProduct().toUpperCase().equals(filterFoodNest.getTypeOfProduct().toUpperCase()))
                .map(this::mapperedToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "foods", key = "#productRequest.productName", condition = "#productRequest.productName != null")
    public List<ProductResponse> findFoodByName(ProductRequest productRequest) {
//        List<Product> products = productRepository.findAllFood();
//        List<ProductResponse> list = new ArrayList<>();
//        for (Product product : products)
//        {
//            if(product.getTypeOfProduct().equals(productRequest.getTypeOfProduct().toUpperCase())
//              && product.getProductName().contains(productRequest.getProductName()))
//            {
//                list.add(mapperedToProductResponse(product));
//            }
//        }
//        return list;
        return productRepository.findAll()
                .stream()
                .filter(product -> product.getTypeOfProduct().equals(productRequest.getTypeOfProduct().toUpperCase())
                        && product.getProductName().contains(productRequest.getProductName()))
                .map(this::mapperedToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> sortFoodByPriceAscending() {
        return productRepository
                .sortProductByPriceAndTypeOfProductAscending("FOOD")
                .stream()
                .map(this::mapperedToProductResponse)
                .collect(Collectors.toList());

    }

    @Override
    public List<ProductResponse> sortFoodByPriceDescending() {
        return productRepository
                .sortProductByPriceAndTypeOfProductDescending("FOOD")
                .stream()
                .map(this::mapperedToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> sortFoodByProductNameAscending() {
        return productRepository
                .sortProductByProductNameAndTypeOfProductAscending("FOOD")
                .stream()
                .map(this::mapperedToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> sortFoodByProductNameDescending() {
        return productRepository
                .sortProductByProductNameAndTypeOfProductDescending("FOOD")
                .stream()
                .map(this::mapperedToProductResponse)
                .collect(Collectors.toList());
    }
}
