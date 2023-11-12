package com.tttm.birdfarmshop.Service.Impl;

import com.tttm.birdfarmshop.Enums.VoucherStatus;
import com.tttm.birdfarmshop.Models.Seller;
import com.tttm.birdfarmshop.Models.Voucher;
import com.tttm.birdfarmshop.Repository.SellerRepository;
import com.tttm.birdfarmshop.Repository.VoucherRepository;
import com.tttm.birdfarmshop.Service.VoucherService;
import com.tttm.birdfarmshop.Utils.Request.FilterVoucher;
import com.tttm.birdfarmshop.Utils.Request.VoucherRequest;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import com.tttm.birdfarmshop.Utils.Response.VoucherResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private final SellerRepository sellerRepository;

    private final Logger logger = LoggerFactory.getLogger(VoucherServiceImpl.class);
    private boolean isValidInformation(VoucherRequest voucherRequest)
    {
        return !voucherRequest.getVoucherName().isEmpty() && !voucherRequest.getVoucherName().isBlank() &&
                !voucherRequest.getStartDate().toString().isEmpty() && !voucherRequest.getVoucherName().toString().isBlank() &&
                !voucherRequest.getEndDate().toString().isEmpty() && !voucherRequest.getEndDate().toString().isBlank() &&
                voucherRequest.getValue() >= 0;
    }
    @Override
    @CacheEvict(value = "vouchers", allEntries = true)
    public MessageResponse createVoucher(VoucherRequest voucherRequest) {
        Voucher voucher = voucherRepository.findVoucherByVoucherName(voucherRequest.getVoucherName());
        if(voucher != null || !isValidInformation(voucherRequest))
        {
            logger.warn("Voucher Name is existed or Invalid Information");
            return new MessageResponse("Fail");
        }

        // Give random seller to take care of voucher
        List<Seller> sellerList = sellerRepository.findAll();
        Random random = new Random();
        int sellerIndex = random.nextInt(sellerList.size());
        Seller seller = sellerList.get(sellerIndex);

        voucherRepository.save(
                Voucher.builder()
                .voucherName(voucherRequest.getVoucherName())
                .startDate(voucherRequest.getStartDate())
                .endDate(voucherRequest.getEndDate())
                .value(voucherRequest.getValue())
                .seller(seller)
                .voucherStatus(VoucherStatus.AVAILABLE)
                .build()
        );
        return new MessageResponse("Success");
    }

    @Override
    public VoucherResponse getVoucherByID(Integer voucherID) {
        return voucherRepository.findById(voucherID)
                .map(voucher -> {
                    if(!isValidVoucher(voucher))
                    {
                        voucher.setVoucherStatus(VoucherStatus.UNAVAILABLE);
                    }
                    else  voucher.setVoucherStatus(VoucherStatus.AVAILABLE);
                    return createVoucherResponse(voucher);
                })
                .orElse(new VoucherResponse());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "voucher", key = "#voucherID"),
            @CacheEvict(value = "vouchers", allEntries = true)
    })
    public MessageResponse updateVoucher(Integer voucherID, VoucherRequest voucherRequest) {
        if(!isValidInformation(voucherRequest))
        {
            return new MessageResponse("Fail");
        }
        return voucherRepository.findById(voucherID)
                .map(voucher -> {
                    voucher.setVoucherName(voucherRequest.getVoucherName());
                    voucher.setStartDate(voucherRequest.getStartDate());
                    voucher.setEndDate(voucherRequest.getEndDate());
                    voucher.setValue(voucherRequest.getValue());
                    if(!isValidVoucher(voucher))
                    {
                        voucher.setVoucherStatus(VoucherStatus.UNAVAILABLE);
                    }
                    else voucher.setVoucherStatus(VoucherStatus.AVAILABLE);
                    voucherRepository.save(voucher);
                    return new MessageResponse("Success");
                })
                .orElse(new MessageResponse("Fail"));
    }

    private boolean isValidVoucher(Voucher voucher)
    {
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            Date systemDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDateString = simpleDateFormat.format(systemDate);
//            logger.info("Current Date: " + currentDateString);
//            logger.info("Start Date: " + voucher.getStartDate());
//            logger.info("End Date: " + voucher.getEndDate());
            if(voucher.getStartDate().toString().equals(currentDateString) || voucher.getEndDate().toString().equals(currentDateString))
            {
                return true;
            }
            if(voucher.getStartDate().compareTo(systemDate) <= 0 && systemDate.compareTo(voucher.getEndDate()) <= 0)
            {
                return true;
            }
            return false;
        }
        catch (Exception exception)
        {
            return false;
        }
    }
    @Override
    @Cacheable(value = "vouchers")
    public List<VoucherResponse> getAllVoucher() {
        return voucherRepository.findAll()
                .stream()
                .map(voucher -> {
                    if(!isValidVoucher(voucher))
                    {
                        voucher.setVoucherStatus(VoucherStatus.UNAVAILABLE);
                    }
                    else  voucher.setVoucherStatus(VoucherStatus.AVAILABLE);
                    voucherRepository.save(voucher);
                    return createVoucherResponse(voucher);
                })
                .collect(Collectors.toList());
    }

    private VoucherResponse createVoucherResponse(Voucher voucher)
    {
        return VoucherResponse.builder()
                .voucherID(voucher.getVoucherID())
                .voucherName(voucher.getVoucherName())
                .startDate(voucher.getStartDate())
                .endDate(voucher.getEndDate())
                .value(voucher.getValue())
                .sellerID(voucher.getSeller().getSellerID())
                .voucherStatus(voucher.getVoucherStatus().toString())
                .build();
    }

    @Override
    @Cacheable(value = "vouchers", key = "#name", condition = "#name != null")
    public List<VoucherResponse> findVoucherByName(String name) {
        return voucherRepository
                .findAll()
                .stream()
                .filter(voucher -> voucher.getVoucherName().contains(name))
                .map(this::createVoucherResponse)
                .collect(Collectors.toList());
    }

    private Date convertDateToFormatPattern(Date date, String target) throws ParseException {
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd HH:mm:ss z yyyy");
        TimeZone timeZone = TimeZone.getTimeZone("ICT");
        outputFormat.setTimeZone(timeZone);
        String outputDateString = outputFormat.format(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (target.equals("start")) {
            // Set the time to 00:00:00
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        } else if (target.equals("end")) {
            // Set the time to 23:59:59
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
        } else {
            throw new IllegalArgumentException("Invalid target: " + target);
        }

        return calendar.getTime();
    }

    @Override
    public List<VoucherResponse> searchVoucherByDate(FilterVoucher filterVoucher) throws ParseException {
        Date startDate = convertDateToFormatPattern(filterVoucher.getStartDate(), "start");
        Date endDate = convertDateToFormatPattern(filterVoucher.getEndDate(), "end");

        if(startDate.compareTo(endDate) > 0)  return null;

        return voucherRepository.findAll()
                .stream()
                .filter(voucher -> {
                    try {
                        Date voucherStartDate = convertDateToFormatPattern(voucher.getStartDate(), "start");
                        Date voucherEndDate = convertDateToFormatPattern(voucher.getEndDate(), "end");
                        return startDate.compareTo(voucherStartDate) <= 0 && endDate.compareTo(voucherEndDate) >= 0;
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(this::createVoucherResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VoucherResponse> sortVoucherByPriceAscending() {
        return voucherRepository.sortVoucherByPriceAscending()
                .stream()
                .map(this::createVoucherResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VoucherResponse> sortVoucherByPriceDescending() {
        return voucherRepository.sortVoucherByPriceDescending()
                .stream()
                .map(this::createVoucherResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VoucherResponse> sortVoucherByAlphabetAscending() {
        return voucherRepository.sortVoucherByAlphabetAscending()
                .stream()
                .map(this::createVoucherResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VoucherResponse> sortVoucherByAlphabetDescending() {
        return voucherRepository.sortVoucherByAlphabetDescending()
                .stream()
                .map(this::createVoucherResponse)
                .collect(Collectors.toList());
    }
}
