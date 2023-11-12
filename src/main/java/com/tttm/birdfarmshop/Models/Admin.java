package com.tttm.birdfarmshop.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "[Admin]")
public class Admin{
  @Id
  @Column(name = "adminID", unique = true, nullable = false)
  private Integer adminID;

  @OneToOne
  @JoinColumn(name = "adminID", referencedColumnName = "userID", insertable = false, updatable = false)
  private User user;

  public Admin(Integer adminID) {
    this.adminID = adminID;
  }
}
