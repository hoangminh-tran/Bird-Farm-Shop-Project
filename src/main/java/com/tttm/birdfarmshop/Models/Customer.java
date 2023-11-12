package com.tttm.birdfarmshop.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "[Customer]")
public class Customer {
  @Id
  @Column(name = "customerID", nullable = false, unique = true)
  private Integer customerID;

  @Column(name = "numberCancleOrder")
  private Integer numberCancleOrder;

  @OneToOne
  @JoinColumn(name = "customerID", referencedColumnName = "userID", updatable = false, insertable = false)
  private User user;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "sellerID")
  private Seller seller;

  @OneToMany(mappedBy = "customer")
  private List<Order> orderList;

  public Customer(Integer customerID) {
    numberCancleOrder = 0;
    this.customerID = customerID;
  }
}
