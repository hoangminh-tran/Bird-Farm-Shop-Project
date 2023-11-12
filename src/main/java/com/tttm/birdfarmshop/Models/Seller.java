package com.tttm.birdfarmshop.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "[Seller]")
public class Seller {
  @Id
  @Column(name = "sellerID", unique = true, nullable = false)
  private Integer sellerID;

  @OneToMany(mappedBy = "seller")
  private List<Customer> customerList;

  @OneToOne
  @JoinColumn(name = "sellerID", referencedColumnName = "userID", insertable = false, updatable = false)
  private User user;

  @OneToMany(mappedBy = "seller")
  private List<News> newsList;

  public Seller(Integer sellerID) {
    this.sellerID = sellerID;
  }
}
