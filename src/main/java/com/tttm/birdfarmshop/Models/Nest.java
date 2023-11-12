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
@Table(name = "[Nest]")
public class Nest {
    @Id
    @Column(name = "nestID", unique = true, nullable = false)
    private String nestID;

    @OneToOne
    @JoinColumn(name = "nestID", referencedColumnName = "productID")
    private Product product;

    public Nest(String nestID) {
        this.nestID = nestID;
    }
}
