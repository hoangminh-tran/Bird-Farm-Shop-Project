package com.tttm.birdfarmshop.Service;

import com.tttm.birdfarmshop.Utils.Request.FilterVoucher;
import com.tttm.birdfarmshop.Utils.Request.VoucherRequest;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import com.tttm.birdfarmshop.Utils.Response.ProductResponse;
import com.tttm.birdfarmshop.Utils.Response.VoucherResponse;

import java.text.ParseException;
import java.util.List;

public interface VoucherService {
    MessageResponse createVoucher(VoucherRequest voucherRequest);
    VoucherResponse getVoucherByID(Integer voucherID);
    MessageResponse updateVoucher(Integer voucherID, VoucherRequest voucherRequest);
    List<VoucherResponse> getAllVoucher();
    List<VoucherResponse> findVoucherByName(String name);

    List<VoucherResponse> searchVoucherByDate(FilterVoucher filterVoucher) throws ParseException;
    List<VoucherResponse> sortVoucherByPriceAscending();
    List<VoucherResponse> sortVoucherByPriceDescending();
    List<VoucherResponse> sortVoucherByAlphabetAscending();
    List<VoucherResponse> sortVoucherByAlphabetDescending();
}
