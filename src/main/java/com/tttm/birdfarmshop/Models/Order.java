package com.tttm.birdfarmshop.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tttm.birdfarmshop.Enums.OrderStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "[Orders]")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customerID")
    private Customer customer;
    @Column(name = "customerPhone", nullable = false, unique = false)
    private String customerPhone;
    @Column(name = "customerName", unique = false, nullable = false)
    private String customerName;
    @Column(name = "customerEmail", unique = false, nullable = false)
    private String customerEmail;
    @Column(name = "customerAddress", nullable = false, unique = false)
    private String customerAddress;
    @Column(name = "status", nullable = false)
    private OrderStatus status;
    @Column(name = "items", nullable = false)
    private String items;
    @Column(name = "amount", nullable = false)
    private Integer amount;
    @Column(name = "description")
    private String description;
    @Column(name = "payment_link")
    private String payment_link;
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created_at;
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updated_at;
    @ManyToOne
    @JoinColumn(name = "shipperID")
    private Shipper shipper;
}
