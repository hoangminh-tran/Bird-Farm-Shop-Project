package com.tttm.birdfarmshop.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "[News]")
public class News {
  @Id
  @Column(name = "newsID", unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer newsID;

  @Column(name = "title", unique = false, nullable = false)
  private String title;

  @Column(name = "image", unique = false, nullable = true)
  private String image;

  @Column(name = "content", unique = false, nullable = true)
  private String content;

  @Column(name = "date", unique = false, nullable = false)
  @Temporal(TemporalType.DATE)
  private Date date;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "sellerID")
  private Seller seller;
}
