package com.tttm.birdfarmshop.Utils.Response;

import com.tttm.birdfarmshop.Enums.VoucherStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoucherResponse {
    private Integer voucherID;
    private String voucherName;
    private Date startDate;
    private Date endDate;
    private Float value;
    private Integer sellerID;
    private String voucherStatus;
}
