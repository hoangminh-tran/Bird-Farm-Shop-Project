package com.tttm.birdfarmshop.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "[Shipper]")
public class Shipper {
  @Id
  @Column(name = "shipperID", unique = true, nullable = false)
  private Integer shipperID;

  @OneToOne
  @JoinColumn(name = "shipperID", referencedColumnName = "userID", insertable = false, updatable = false)
  private User user;

  public Shipper(Integer shipperID) {
    this.shipperID = shipperID;
  }
}
