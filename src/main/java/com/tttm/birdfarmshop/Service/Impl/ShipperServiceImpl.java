package com.tttm.birdfarmshop.Service.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.birdfarmshop.DTO.OrderDTO;
import com.tttm.birdfarmshop.Enums.AccountStatus;
import com.tttm.birdfarmshop.Enums.OrderStatus;
import com.tttm.birdfarmshop.Enums.ProductStatus;
import com.tttm.birdfarmshop.Enums.VoucherStatus;
import com.tttm.birdfarmshop.Models.*;
import com.tttm.birdfarmshop.Repository.CustomerRepository;
import com.tttm.birdfarmshop.Repository.OrderRepository;
import com.tttm.birdfarmshop.Repository.ShipperRepository;
import com.tttm.birdfarmshop.Service.ShipperService;
import com.tttm.birdfarmshop.Utils.Body;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import com.tttm.birdfarmshop.Utils.Utils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipperServiceImpl implements ShipperService {
    private final ShipperRepository shipperRepository;
    private static final Logger logger = LogManager.getLogger(ShipperServiceImpl.class);

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void createShipper(User user) {
        shipperRepository.save(new Shipper(user.getUserID()));
        logger.info("Create new Shipper Successfully");
    }

    @Override
    public List<OrderDTO> getShippedOrder(Integer shipperID) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<OrderDTO> list = orderRepository.getOrderByShipperID(shipperID)
                    .stream()
                    .map(order -> OrderDTO.builder()
                            .id(order.getId())
                            .customerPhone(order.getCustomerPhone())
                            .customerName(order.getCustomerName())
                            .customerEmail(order.getCustomerEmail())
                            .customerAddress(order.getCustomerAddress())
                            .status(order.getStatus())
                            .items(order.getItems())
                            .amount(order.getAmount())
                            .description(order.getDescription())
                            .payment_link(order.getPayment_link())
                            .build())
                    .collect(Collectors.toList());
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public MessageResponse updateShippedOrder(Integer orderID) {
        Optional<Order> orderOptional = orderRepository.findById(orderID);
        if (orderOptional.isEmpty()) return new MessageResponse("Fail to Update");

//        ProductStatus status = productOptional.map(Product::getProductStatus).orElse(null);
        OrderStatus status = orderOptional.map(Order::getStatus).orElse(null);
        if (status != null) {
            if (status.equals(OrderStatus.COMPLETED)) {
                orderOptional.get().setStatus(OrderStatus.CANCELED);
            } else if (status.equals(OrderStatus.PENDING)) {
                orderOptional.get().setStatus(OrderStatus.PROCESSING);
            } else if (status.equals(OrderStatus.PROCESSING)) {
                orderOptional.get().setStatus(OrderStatus.COMPLETED);
            } else if (status.equals(OrderStatus.CANCELED)) {
                orderOptional.get().setStatus(OrderStatus.PENDING);
            }
            orderRepository.save(orderOptional.get());
            return new MessageResponse("Update Successfully");
        }
        return new MessageResponse("Fail to Update");
    }
}
