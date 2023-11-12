package com.tttm.birdfarmshop.Models;

import com.tttm.birdfarmshop.Enums.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "[Product]")
public class Product {
  @Id
  @Column(name = "productID", unique = true, nullable = false)
  private String productID;

  @Column(name = "product_name", unique = false, nullable = false)
  private String productName;

  @Column(name = "price", unique = false, nullable = false)
  private Integer price;

  @Column(name = "description", unique = false, nullable = true)
  private String description;

  @Column(name = "type_of_product", unique = false, nullable = false)
  private String typeOfProduct;

  @Column(name = "feedback", unique = false, nullable = true)
  private String feedback;

  @Column(name = "rating", unique = false, nullable = false)
  private Integer rating;

  @Column(name = "product_status", unique = false, nullable = false)
  private ProductStatus productStatus;

  @Column(name = "quantity", unique = false, nullable = false)
  private Integer quantity;

  @OneToMany(mappedBy = "imageProduct")
  private List<Image> listImages;
}
