package com.tttm.birdfarmshop.Models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invoice {
  private String userID;
  private String customerPhone;
  private String customerEmail;
  private String customerAddress;
  private List<Product> productList;
  private Integer price;
  private List<Voucher> voucherList;
  private Integer totalPrice;
  private LocalDateTime orderDate;
  private Shipper shipper;
}
