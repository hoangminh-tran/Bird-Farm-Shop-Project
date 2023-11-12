package com.tttm.birdfarmshop.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Entity
//@Table(name = "[ProductManager]")
public class ProductManager {
//  @Id
//  @Column(name = "productManagerID", unique = true, nullable = false)
  private Integer productManagerID;

//  @OneToOne
//  @JoinColumn(name = "productManagerID", referencedColumnName = "userID", insertable = false, updatable = false)
  private User user;
}
