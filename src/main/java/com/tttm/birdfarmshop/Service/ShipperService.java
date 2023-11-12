package com.tttm.birdfarmshop.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.birdfarmshop.DTO.OrderDTO;
import com.tttm.birdfarmshop.Models.Order;
import com.tttm.birdfarmshop.Models.User;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;

import java.util.List;

public interface ShipperService {
    void createShipper(User user);
    List<OrderDTO> getShippedOrder(Integer shipperID);
    MessageResponse updateShippedOrder(Integer orderID);
}
