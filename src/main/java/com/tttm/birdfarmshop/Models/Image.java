package com.tttm.birdfarmshop.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imageID", nullable = false, unique = true)
    private Integer imageID;

    @Column(name = "imageUrl", nullable = true, unique = false)
    private String imageUrl;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "productID", referencedColumnName = "productID")
    private Product imageProduct;

    public Image(String imageUrl, Product imageProduct) {
        this.imageUrl = imageUrl;
        this.imageProduct = imageProduct;
    }
}
