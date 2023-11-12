package com.tttm.birdfarmshop.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.birdfarmshop.Constant.ConstantAPI;
import com.tttm.birdfarmshop.Constant.ConstantParametter;
import com.tttm.birdfarmshop.DTO.OrderDTO;
import com.tttm.birdfarmshop.Models.Order;
import com.tttm.birdfarmshop.Service.OrderService;
import com.tttm.birdfarmshop.Service.ShipperService;
import com.tttm.birdfarmshop.Utils.Response.BirdResponse;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ConstantAPI.SHIPPER)
public class ShipperController {
    private final ShipperService shipperService;

    @GetMapping(ConstantAPI.GET_SHIPPED_ORDER + ConstantParametter.SHIPPER_ID)
    public ResponseEntity<List<OrderDTO>> getShippedOrder(@PathVariable("ShipperID") Integer ShipperID) throws com.tttm.birdfarmshop.Exception.CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(shipperService.getShippedOrder(ShipperID));
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(ConstantAPI.UPDATE_ORDER_STATUS_SHIPPER + ConstantParametter.ORDER_ID)
    public ResponseEntity<MessageResponse> updateShippedOrder(@PathVariable("OrderID") Integer orderID) throws com.tttm.birdfarmshop.Exception.CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return new ResponseEntity<>(shipperService.updateShippedOrder(orderID), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
