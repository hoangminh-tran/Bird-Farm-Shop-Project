package com.tttm.birdfarmshop.Models;

import com.tttm.birdfarmshop.Enums.VoucherStatus;
import jakarta.persistence.*;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "[Voucher]")
public class Voucher {
  @Id
  @Column(name = "voucherID", nullable = false, unique = true)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer voucherID;

  @Column(name = "voucherName", nullable = false, unique = true)
  private String voucherName;

  @Column(name = "startDate", nullable = false, unique = false)
  @Temporal(TemporalType.DATE)
  private Date startDate;

  @Column(name = "endDate", nullable = false, unique = false)
  @Temporal(TemporalType.DATE)
  private Date endDate;

  @Column(name = "value", nullable = false, unique = false)
  private Float value;

  @ManyToOne
  @JoinColumn(name = "sellerID")
  private Seller seller;

  @Column(name = "voucherStatus", nullable = false, unique = false)
  private VoucherStatus voucherStatus;

  public Voucher(String voucherName, Date startDate, Date endDate, Float value, Seller seller, VoucherStatus voucherStatus) {
    this.voucherName = voucherName;
    this.startDate = startDate;
    this.endDate = endDate;
    this.value = value;
    this.seller = seller;
    this.voucherStatus = voucherStatus;
  }
}
