package com.tttm.birdfarmshop.Utils.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherRequest {
    private String voucherName;
    private Date startDate;
    private Date endDate;
    private Float value;
}
