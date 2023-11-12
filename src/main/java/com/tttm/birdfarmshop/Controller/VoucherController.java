package com.tttm.birdfarmshop.Controller;

import com.tttm.birdfarmshop.Constant.ConstantAPI;
import com.tttm.birdfarmshop.Constant.ConstantParametter;
import com.tttm.birdfarmshop.Exception.CustomException;
import com.tttm.birdfarmshop.Service.VoucherService;
import com.tttm.birdfarmshop.Utils.Request.FilterVoucher;
import com.tttm.birdfarmshop.Utils.Request.VoucherRequest;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import com.tttm.birdfarmshop.Utils.Response.ProductResponse;
import com.tttm.birdfarmshop.Utils.Response.VoucherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ConstantAPI.VOUCHER)
public class VoucherController {
    private final VoucherService voucherService;

    @PostMapping(ConstantAPI.CREATE_VOUCHER)
    public ResponseEntity<MessageResponse> createVoucher(@RequestBody VoucherRequest dto) throws CustomException {
        try {
            return new ResponseEntity<>(voucherService.createVoucher(dto), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    @PutMapping(ConstantAPI.UPDATE_VOUCHER + ConstantParametter.VOUCHER_ID)
    public ResponseEntity<MessageResponse> updateVoucher(@PathVariable ("VoucherID") Integer VoucherID,
                                                            @RequestBody VoucherRequest dto) throws CustomException {
        try {
            return new ResponseEntity<>(voucherService.updateVoucher(VoucherID, dto), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    @GetMapping(ConstantAPI.GET_VOUCHER_BY_ID + ConstantParametter.VOUCHER_ID)
    public ResponseEntity<VoucherResponse> getVoucherByID(@PathVariable ("VoucherID") Integer VoucherID) throws CustomException {
        try {
            return new ResponseEntity<>(voucherService.getVoucherByID(VoucherID), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ConstantAPI.GET_ALL_VOUCHER)
    public ResponseEntity<List<VoucherResponse>> getAllVoucher() throws CustomException {
        try {
            return new ResponseEntity<>(voucherService.getAllVoucher(), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ConstantAPI.FILTER_VOUCHER_BY_DAY)
    public ResponseEntity<List<VoucherResponse>> searchVoucherByDate(@RequestBody FilterVoucher filterVoucher) throws CustomException {
        try {
            return new ResponseEntity<>(voucherService.searchVoucherByDate(filterVoucher), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ConstantAPI.GET_VOUCHER_BY_NAME + ConstantParametter.VOUCHER_NAME)
    public ResponseEntity<List<VoucherResponse>> getVoucherByName(@PathVariable("VoucherName") String VoucherName) throws CustomException {
        try {
            return new ResponseEntity<>(voucherService.findVoucherByName(VoucherName), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping(ConstantAPI.SORT_VOUCHER_BY_PRICE_ASC)
    public ResponseEntity<List<VoucherResponse>> sortFoodByPriceAscending() throws CustomException {
        try {
            return new ResponseEntity<>(voucherService.sortVoucherByPriceAscending(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ConstantAPI.SORT_VOUCHER_BY_PRICE_DESC)
    public ResponseEntity<List<VoucherResponse>> sortFoodByPriceDescending() throws CustomException {
        try {
            return new ResponseEntity<>(voucherService.sortVoucherByPriceDescending(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ConstantAPI.SORT_VOUCHER_BY_ALPHABET_ASC)
    public ResponseEntity<List<VoucherResponse>> sortVoucherByAlphabetAscending() throws CustomException {
        try {
            return new ResponseEntity<>(voucherService.sortVoucherByAlphabetAscending(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ConstantAPI.SORT_VOUCHER_BY_ALPHABET_DESC)
    public ResponseEntity<List<VoucherResponse>> sortVoucherByAlphabetDescending() throws CustomException {
        try {
            return new ResponseEntity<>(voucherService.sortVoucherByAlphabetDescending(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
