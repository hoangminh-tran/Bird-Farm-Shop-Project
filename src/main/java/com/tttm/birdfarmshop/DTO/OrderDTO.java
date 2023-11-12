package com.tttm.birdfarmshop.DTO;

import com.tttm.birdfarmshop.Enums.OrderStatus;
import com.tttm.birdfarmshop.Models.Customer;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Integer id;

    private String customerPhone;

    private String customerName;

    private String customerEmail;

    private String customerAddress;

    private OrderStatus status;

    private String items;

    private Integer amount;

    private String description;

    private String payment_link;
}
