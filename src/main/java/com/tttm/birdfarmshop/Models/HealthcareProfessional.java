package com.tttm.birdfarmshop.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "[HealthcareProfessional]")
public class HealthcareProfessional {
  @Id
  @Column(name = "healthcareID", nullable = false, unique = true)
  private Integer healthcareID;

  @OneToOne
  @JoinColumn(name = "healthcareID", referencedColumnName = "userID", insertable = false, updatable = false)
  private User user;

  @JsonIgnore
  @OneToMany(mappedBy = "healthcareProfessional")
  private List<Bird> birdList;

  public HealthcareProfessional(Integer healthcareID) {
    this.healthcareID = healthcareID;
  }
}
