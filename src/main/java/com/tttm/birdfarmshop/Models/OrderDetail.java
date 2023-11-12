package com.tttm.birdfarmshop.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "[OrderDetail]")
public class OrderDetail {
  @Id
  @Column(name = "orderDetailID", unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer orderDetailID;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "productID")
  private Product product;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "orderID")
  private Order order;

  public OrderDetail(Product product, Order order) {
    this.product = product;
    this.order = order;
  }
}
