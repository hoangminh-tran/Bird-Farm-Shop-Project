package com.tttm.birdfarmshop.Repository;

import com.tttm.birdfarmshop.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Modifying
    @Query(value = "update orders set status = 2 where id = ?1", nativeQuery = true)
    void deleteOrderByOrderID(Integer orderID);

    @Query(
            value = " select * from `orders`", nativeQuery = true
    )
    List<Order> getAllOrder();

    @Query(
            value = " select * from `orders` where shipperid = ?1", nativeQuery = true
    )
    List<Order> getOrderByShipperID(Integer shipperID);

    @Query(
            value = " select * from orders o join customer c on c.customerID = o.customerID" +
                    " where c.customerID = ?1", nativeQuery = true
    )
    List<Order> viewOrderHistoryCustomer(Integer customerId);
}
