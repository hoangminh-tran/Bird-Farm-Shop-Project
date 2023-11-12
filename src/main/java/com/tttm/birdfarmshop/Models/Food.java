package com.tttm.birdfarmshop.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "[Food]")
public class Food {
    @Id
    @Column(name = "foodID", unique = true, nullable = false)
    private String foodID;

    @OneToOne
    @JoinColumn(name = "foodID", referencedColumnName = "productID")
    private Product product;

    public Food(String foodID) {
        this.foodID = foodID;
    }
}
