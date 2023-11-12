package com.tttm.birdfarmshop.Service.Impl;

import com.tttm.birdfarmshop.DTO.FoodDTO;
import com.tttm.birdfarmshop.DTO.NestDTO;
import com.tttm.birdfarmshop.Enums.ProductStatus;
import com.tttm.birdfarmshop.Models.Image;
import com.tttm.birdfarmshop.Models.Nest;
import com.tttm.birdfarmshop.Models.Product;
import com.tttm.birdfarmshop.Repository.ImageRepository;
import com.tttm.birdfarmshop.Repository.NestRepository;
import com.tttm.birdfarmshop.Repository.ProductRepository;
import com.tttm.birdfarmshop.Service.NestService;
import com.tttm.birdfarmshop.Utils.Request.FilterFoodNest;
import com.tttm.birdfarmshop.Utils.Request.ProductRequest;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import com.tttm.birdfarmshop.Utils.Response.ProductResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NestServiceImpl implements NestService {
    private final NestRepository nestRepository;
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private boolean isValidFood(NestDTO dto)
    {
        return !dto.getProductName().isBlank() && !dto.getProductName().isEmpty() && dto.getPrice() >= 0
                && !dto.getTypeOfProduct().isEmpty() && !dto.getTypeOfProduct().isBlank() && dto.getRating() >= 0;
    }
    @Override
    @CacheEvict(value = "nests", allEntries = true)
    public MessageResponse AddNewNest(NestDTO dto) {
        int size = (int) nestRepository.findAll().stream().count();
        String NestID = "N00" + (size + 1);

        if(isValidFood(dto))
        {
            Product product = productRepository.save( Product.builder()
                    .productID(NestID)
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

            nestRepository.save(new Nest(NestID));
            return new MessageResponse("Success");
        }
        return new MessageResponse("Fail");
    }

    @Transactional
    @Override
    @Caching(
            put = {
                    @CachePut(value = "nest", key = "#nestID", condition = "#nestID != null")
            },
            evict = {
                    @CacheEvict(value = "nests", allEntries = true)
            })
    public MessageResponse UpdateNest(String nestID, NestDTO dto) {
        try {
            Optional<Product> productOptional = productRepository.findById(nestID);
            if(productOptional.isEmpty() || !isValidFood(dto))
            {
                return new MessageResponse("Fail");
            }
            return new MessageResponse(
                    Optional
                            .ofNullable(productRepository.findById(nestID).get())
                            .map(nest ->{
                                nest.setProductName(dto.getProductName());
                                nest.setPrice(dto.getPrice());
                                nest.setDescription(dto.getDescription());
                                nest.setTypeOfProduct(dto.getTypeOfProduct().toUpperCase());
                                nest.setFeedback(dto.getFeedback());
                                nest.setRating(dto.getRating());
                                nest.setQuantity(dto.getQuantity());
                                productRepository.save(nest);

                                List<String> listImages = dto.getImages();
                                if(listImages.size() != 0)
                                {
                                    imageRepository.deleteImageByproductID(nestID);
                                    listImages.forEach(
                                            imageUrl -> imageRepository.save(
                                                    Image.builder()
                                                            .imageUrl(imageUrl)
                                                            .imageProduct(nest)
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
                .typeOfProduct(product.getTypeOfProduct())
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
    @Cacheable(value = "nest", key = "#nestID", condition = "#nestID != null")
    public ProductResponse findNestByNestID(String nestID) {
        try
        {
            Optional<Product> nestOptional = productRepository.findById(nestID);
            if(nestOptional.isPresent())
            {
                return mapperedToProductResponse(nestOptional.get());
            }
            else return new ProductResponse();
        }
        catch (Exception e)
        {
            return new ProductResponse();
        }
    }

    @Override
    @Cacheable(value = "nests")
    public List<ProductResponse> findAllNest() {
        return productRepository.findAllNest()
                .stream()
                .map(this::mapperedToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> findNestByPrice(FilterFoodNest filterFoodNest) {
        if(filterFoodNest.getLeft_range_price() > filterFoodNest.getRight_range_price()) return null;

        return productRepository.findAll()
                .stream()
                .filter(product -> product.getPrice() <= filterFoodNest.getRight_range_price() && product.getPrice() >= filterFoodNest.getLeft_range_price()
                        && product.getTypeOfProduct().toUpperCase().equals(filterFoodNest.getTypeOfProduct().toUpperCase()))
                .map(this::mapperedToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "nests", key = "#productRequest.productName", condition = "#productRequest.productName != null")
    public List<ProductResponse> findNestByName(ProductRequest productRequest) {
        return productRepository.findAll()
                .stream()
                .filter(product -> product.getTypeOfProduct().equals(productRequest.getTypeOfProduct().toUpperCase())
                        && product.getProductName().contains(productRequest.getProductName()))
                .map(this::mapperedToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> sortNestByPriceAscending() {
        return productRepository
                .sortProductByPriceAndTypeOfProductAscending("NEST")
                .stream()
                .map(this::mapperedToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> sortNestByPriceDescending() {
        return productRepository
                .sortProductByPriceAndTypeOfProductDescending("NEST")
                .stream()
                .map(this::mapperedToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> sortNestByProductNameAscending() {
        return productRepository
                .sortProductByProductNameAndTypeOfProductAscending("NEST")
                .stream()
                .map(this::mapperedToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> sortNestByProductNameDescending() {
        return productRepository
                .sortProductByProductNameAndTypeOfProductDescending("NEST")
                .stream()
                .map(this::mapperedToProductResponse)
                .collect(Collectors.toList());
    }
}
