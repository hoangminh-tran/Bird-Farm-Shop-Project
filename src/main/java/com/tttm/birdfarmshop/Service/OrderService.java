package com.tttm.birdfarmshop.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.birdfarmshop.Models.Order;
import com.tttm.birdfarmshop.Utils.Request.OrderRequest;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import com.tttm.birdfarmshop.Utils.Response.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderService {
//    OrderResponse createOrder(OrderRequest orderRequest);
    ObjectNode getOrderByOrderID(Integer OrderID);
    Order findOrderByID(Integer orderID);
    ObjectNode getAllOrder();
    MessageResponse DeleteOrder(Integer OrderID);
    ObjectNode CreateOrder(OrderRequest order);

    ObjectNode updatePaymentForOrder(int orderId);
    List<OrderResponse> viewOrderHistoryCustomer(int customerID);
}
