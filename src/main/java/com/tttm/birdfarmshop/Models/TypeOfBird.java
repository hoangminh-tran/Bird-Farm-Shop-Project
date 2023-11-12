package com.tttm.birdfarmshop.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "[TypeOfBird]")
public class TypeOfBird {

  @Id
  @Column(name = "typeID", nullable = false, unique = true)
  private String typeID;

  @Column(name = "typeName", nullable = false, unique = false)
  private String typeName;

  @Column(name = "quantity", unique = false, nullable = false)
  private Integer quantity;

  @OneToMany(mappedBy = "typeOfBird")
  private List<Bird> birdList;

  public TypeOfBird(String typeID, String typeName, Integer quantity) {
    this.typeID = typeID;
    this.typeName = typeName;
    this.quantity = quantity;
  }
}
