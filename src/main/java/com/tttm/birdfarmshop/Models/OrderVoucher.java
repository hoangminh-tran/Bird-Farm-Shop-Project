package com.tttm.birdfarmshop.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "[OrderVoucher]")
public class OrderVoucher {
    @Id
    @Column(name = "orderVoucherID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderVoucherID;

    @ManyToOne
    @JoinColumn(name = "orderID")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "voucherID")
    private Voucher voucher;

    public OrderVoucher(Order order, Voucher voucher) {
        this.order = order;
        this.voucher = voucher;
    }
}
