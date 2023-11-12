package com.tttm.birdfarmshop.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tttm.birdfarmshop.Enums.BirdColor;
import jakarta.persistence.*;
import lombok.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "[Bird]")
public class Bird {
  @Id
  @Column(name = "birdID", nullable = false, unique = true)
  private String birdID;

  @Column(name = "age", nullable = true, unique = false)
  private Integer age;

  @Column(name = "gender", nullable = true, unique = false)
  private Boolean gender;

  @Column(name = "ownerID", nullable = false, unique = false)
  private Integer ownerID;

  @Column(name = "fertility", nullable = false, unique = false)
  private Boolean fertility;

  @Column(name = "breedingTimes", nullable = true, unique = false)
  private Integer breedingTimes;

  @Column(name = "color", nullable = true, unique = false)
  private BirdColor color;

  @OneToOne
  @JoinColumn(name = "birdID", referencedColumnName = "productID", insertable = false, updatable = false)
  private Product product;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "typeID")
  private TypeOfBird typeOfBird;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "healthcareID")
  private HealthcareProfessional healthcareProfessional;

  public Bird(String birdID, Integer age, Boolean gender, Integer status, Boolean fertility, TypeOfBird typeOfBird, HealthcareProfessional healthcareProfessional) {
    this.birdID = birdID;
    this.age = age;
    this.gender = gender;
    this.ownerID = status;
    this.fertility = fertility;
    this.typeOfBird = typeOfBird;
    this.healthcareProfessional = healthcareProfessional;
  }

  public Bird(String birdID, Integer age, Boolean gender, Integer status, Boolean fertility) {
    this.birdID = birdID;
    this.age = age;
    this.gender = gender;
    this.ownerID = status;
    this.fertility = fertility;
  }
}
