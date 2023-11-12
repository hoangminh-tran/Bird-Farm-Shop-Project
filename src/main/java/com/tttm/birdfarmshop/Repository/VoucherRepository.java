package com.tttm.birdfarmshop.Repository;

import com.tttm.birdfarmshop.Models.Product;
import com.tttm.birdfarmshop.Models.User;
import com.tttm.birdfarmshop.Models.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    @Query(
            value = "select * from voucher where voucher_name = ?1", nativeQuery = true
    )
    Voucher findVoucherByVoucherName(String voucherName);

    @Query(
            value = "SELECT * FROM voucher order by value DESC", nativeQuery = true
    )
    List<Voucher> sortVoucherByPriceDescending();

    @Query(
            value = "SELECT * FROM voucher order by value ASC", nativeQuery = true
    )
    List<Voucher> sortVoucherByPriceAscending();

    @Query(
            value = "SELECT * FROM voucher order by voucher_name DESC", nativeQuery = true
    )
    List<Voucher> sortVoucherByAlphabetDescending();

    @Query(
            value = "SELECT * FROM voucher order by voucher_name ASC", nativeQuery = true
    )
    List<Voucher> sortVoucherByAlphabetAscending();
}
