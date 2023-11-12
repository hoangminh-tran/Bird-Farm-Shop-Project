package com.tttm.birdfarmshop.Repository;

import com.tttm.birdfarmshop.Models.OrderVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderVoucherRepository extends JpaRepository<OrderVoucher, Integer> {
    @Query(
            value = "select * from order_voucher where orderid = ?1", nativeQuery = true
    )
    List<OrderVoucher> findOrderVoucherByOrderID(Integer orderId);

    @Modifying
    @Query(value = "delete from order_voucher where orderid = ?1", nativeQuery = true)
    void deleteOrderVoucherByOrderID(Integer orderID);

}
