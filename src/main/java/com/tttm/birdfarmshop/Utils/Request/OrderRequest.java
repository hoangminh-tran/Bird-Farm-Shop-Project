package com.tttm.birdfarmshop.Utils.Request;

import com.tttm.birdfarmshop.Models.Customer;
import com.tttm.birdfarmshop.Models.Product;
import com.tttm.birdfarmshop.Models.Shipper;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private Integer customerID;
    private String customerPhone;
    private String customerName;
    private String customerEmail;
    private String customerAddress;
    private String description;
    private List<String> listProduct;  // Store List of Product ID
    private List<String> voucherList;  // List of Voucher ID
}
