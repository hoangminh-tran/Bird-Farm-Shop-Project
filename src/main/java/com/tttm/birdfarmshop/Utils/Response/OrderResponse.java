package com.tttm.birdfarmshop.Utils.Response;

import com.tttm.birdfarmshop.Models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Integer orderID;

    private Integer customerID;

    private String customerPhone;

    private String customerName;

    private String customerEmail;

    private String customerAddress;

    private String note;

    private Integer orderAmount;
    private String OrderStatus;

    private Date orderDate;

    private List<ProductResponse> productList;
}
