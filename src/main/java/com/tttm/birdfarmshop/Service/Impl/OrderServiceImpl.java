package com.tttm.birdfarmshop.Service.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.birdfarmshop.Enums.AccountStatus;
import com.tttm.birdfarmshop.Enums.OrderStatus;
import com.tttm.birdfarmshop.Enums.ProductStatus;
import com.tttm.birdfarmshop.Enums.VoucherStatus;
import com.tttm.birdfarmshop.Exception.CustomException;
import com.tttm.birdfarmshop.Models.*;
import com.tttm.birdfarmshop.Repository.*;
import com.tttm.birdfarmshop.Service.MailService;
import com.tttm.birdfarmshop.Service.OrderService;
import com.tttm.birdfarmshop.Utils.Body;
import com.tttm.birdfarmshop.Utils.Request.OrderRequest;
import com.tttm.birdfarmshop.Utils.Request.SendMailOrderRequest;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import com.tttm.birdfarmshop.Utils.Response.OrderResponse;
import com.tttm.birdfarmshop.Utils.Response.ProductResponse;
import com.tttm.birdfarmshop.Utils.Utils;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    @Value("${PAYOS_CREATE_PAYMENT_LINK_URL}")
    private String createPaymentLinkUrl;
    @Value("${PAYOS_CLIENT_ID}")
    private String clientId;
    @Value("${PAYOS_API_KEY}")
    private String apiKey;
    @Value("${PAYOS_CHECKSUM_KEY}")
    private String checksumKey;

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final OrderVoucherRepository orderVoucherRepository;
    private final CustomerRepository customerRepository;
    private final ShipperRepository shipperRepository;
    private final VoucherRepository voucherRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    @Override
    public ObjectNode CreateOrder(OrderRequest order) {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            final Integer customerID = order.getCustomerID();
            String customerPhone = order.getCustomerPhone();
            String customerName = order.getCustomerName();
            String customerEmail = order.getCustomerEmail();
            String customerAddress = order.getCustomerAddress();
            final String description = order.getDescription();
            final List<String> listProduct = order.getListProduct();  // Store List of Product ID
            final List<String> listVoucher = order.getVoucherList();  // List of Voucher ID

            Optional<Customer> customer = customerRepository.findById(customerID);
            Optional<User> user = userRepository.findById(customerID);
            if(customer == null || user.get().getAccountStatus() == AccountStatus.INACTIVE){
                throw new CustomException("Customer ID is Invalid or Your Account is banned");
            }

            if(customerPhone.trim().isBlank() || customerPhone.trim().isEmpty()){
                customerPhone = user.get().getPhone();
            }
            if(customerName.trim().isBlank() || customerName.trim().isEmpty()){
                customerName = user.get().getFirstName() + user.get().getLastName();
            }
            if(customerEmail.trim().isBlank() || customerEmail.trim().isEmpty()){
                customerEmail = user.get().getEmail();
            }
            if(customerAddress.trim().isBlank() || customerAddress.trim().isEmpty()){
                customerAddress = user.get().getAddress();
            }

            int orderAmount = 0;
            List<Product> productList = new ArrayList<>();
            for(String productID : listProduct)  // Check Product ID is existed or not
            {
                Optional<Product> productOptional = productRepository.findById(productID);
                if (productOptional.isEmpty()) {
                    throw new CustomException("Product ID is Unavailable or out of stock");
                }
                Product product = productOptional.get();
                if (product.getProductStatus() == ProductStatus.UNAVAILABLE || product.getQuantity() <= 0) {
                    throw new CustomException("Product ID is Unavailable or out of stock");
                }
                productList.add(product);
                orderAmount += product.getPrice();
            }

            List<Voucher> voucherList = new ArrayList<>();
            for(String voucherID : listVoucher)
            {
                Optional<Voucher> voucher = voucherRepository.findById(Integer.parseInt(voucherID));
                if(voucher.isEmpty() || voucher.get().getVoucherStatus().equals(VoucherStatus.UNAVAILABLE)){
                    throw new CustomException("Voucher ID is not existed to apply voucher");
                }
                else{
                    orderAmount -= voucher.get().getValue();
                    voucherList.add(voucher.get());
                }
            }

            // Choose random shipper to take care the Order
            List<Shipper> shipperList = shipperRepository.findAll();
            Random random = new Random();
            int randomShipperIndex = random.nextInt(shipperList.size());
            Shipper shipper = shipperList.get(randomShipperIndex);

            String currentTimeString = String.valueOf(String.valueOf(new Date().getTime()));
            int orderCode =
                    Integer.parseInt(currentTimeString.substring(currentTimeString.length() - 6));
            ObjectNode item = objectMapper.createObjectNode();
            item.put("customerID", customerID);
            item.put("customerPhone", customerPhone);
            item.put("customerName", customerName);
            item.put("customerEmail", customerEmail);
            item.put("customerAddress", customerAddress);
            item.put("description", description);
            item.put("listProduct", Arrays.toString(listProduct.toArray()));
            item.put("voucherList", Arrays.toString(listVoucher.toArray()));
            item.put("price", orderAmount);

            orderRepository.save(
                    Order.builder()
                            .customer(customer.get())
                            .customerPhone(customerPhone)
                            .customerName(customerName)
                            .customerEmail(customerEmail)
                            .customerAddress(customerAddress)
                            .status(OrderStatus.PENDING)
                            .items(listProduct.toString())
                            .amount(orderAmount)
                            .description(description)
                            .payment_link("checkoutUrl")
                            .shipper(shipper)
                            .build()
            );

            List<Order> orderList = orderRepository.getAllOrder();
            int size = orderRepository.findAll().size();
            Order newestOrder = orderList.get(size - 1);
            newestOrder.setStatus(OrderStatus.PROCESSING);
            List<ObjectNode> items = List.of(item);
            Body body = new Body(orderCode, orderAmount, description, items, "", "", "");
            String returnUrl = "/order/updatePaymentStatus" + newestOrder.getId();
            body.setReturnUrl("");
            body.setCancelUrl("");
            String bodyToSignature = Utils.createSignatureOfPaymentRequest(body, checksumKey);
            body.setSignature(bodyToSignature);
            // Tạo header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-client-id", clientId);
            headers.set("x-api-key", apiKey);
            // Gửi yêu cầu POST
            WebClient client = WebClient.create();
            Mono<String> response = client.post()
                    .uri(createPaymentLinkUrl)
                    .headers(httpHeaders -> httpHeaders.putAll(headers))
                    .body(BodyInserters.fromValue(body))
                    .retrieve()
                    .bodyToMono(String.class);
            String responseBody = response.block();
            JsonNode res = objectMapper.readTree(responseBody);
            System.out.println(res);
            if (!Objects.equals(res.get("code").asText(), "00")) {
                orderRepository.deleteOrderByOrderID(newestOrder.getId());
                throw new Exception("Fail");
            }
            String checkoutUrl = res.get("data").get("checkoutUrl").asText();

            //Kiểm tra dữ liệu có đúng không
            String paymentLinkResSignature = Utils.createSignatureFromObj(res.get("data"), checksumKey);
            System.out.println(paymentLinkResSignature);
            if (!paymentLinkResSignature.equals(res.get("signature").asText())) {
                orderRepository.deleteOrderByOrderID(newestOrder.getId());
                throw new Exception("Signature is not compatible");
            }

            productList.forEach(product ->
                    {
//                        product.setQuantity(product.getQuantity() - 1);
//                        product.setProductStatus(ProductStatus.UNAVAILABLE);
//                        productRepository.save(product);
                        orderDetailRepository.save(new OrderDetail(product, newestOrder));
                    }
            );

            voucherList.forEach(voucher -> {
                orderVoucherRepository.save(new OrderVoucher(newestOrder, voucher));
            });
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", 0);
            respon.put("message", "success");
            respon.set("data", objectMapper.createObjectNode()
                    .put("checkoutUrl", checkoutUrl)
                    .put("returnUrl", returnUrl)
                    .put("orderStatus", newestOrder.getStatus().name())
                    .put("orderCode", orderCode)
                    .put("orderID", newestOrder.getId())
            );

//            Order updatePaymentLinkOrder = orderRepository.getById(newestOrder.getId());
            newestOrder.setPayment_link(checkoutUrl);
            orderRepository.save(newestOrder);
            return respon;

        }catch (Exception ex){
            ex.printStackTrace();
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @Override
    public ObjectNode updatePaymentForOrder(int orderId) {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            Order order = orderRepository.getReferenceById(orderId);
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);

            List<OrderDetail> orderDetailList = orderDetailRepository.findAllOrderDetailByOrderID(orderId);
            for (OrderDetail orderDetail : orderDetailList){
                Product product = orderDetail.getProduct();
                product.setQuantity(product.getQuantity() - 1);
                if(product.getQuantity() < 1)
                    product.setProductStatus(ProductStatus.UNAVAILABLE);
                productRepository.save(product);
                orderDetail.setProduct(product);
//                orderDetailRepository.save(new OrderDetail(product, order));
            }

            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Payment Successfully");
            respon.put("orderStatus", OrderStatus.COMPLETED.name());
            SendMailOrderRequest sendMailOrderRequest = new SendMailOrderRequest(
                    order.getCustomer().getCustomerID(),
                    order.getCustomer().getUser().getEmail(),
                    orderId);
            mailService.sendMailForCompleteOrder(sendMailOrderRequest);

            return respon;
        }catch (Exception ex){
            ex.printStackTrace();
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @Override
    public ObjectNode getOrderByOrderID(Integer OrderID) {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            // Tìm đơn hàng từ cơ sở dữ liệu bằng ID
            Order order = orderRepository.findById(OrderID).orElse(null);
            ObjectNode resultNode = JsonNodeFactory.instance.objectNode();
            if (order != null) {
                resultNode.put("id", order.getId());
                resultNode.put("customerName", order.getCustomerName());
                resultNode.put("customerEmail", order.getCustomerEmail());
                resultNode.put("customerPhone", order.getCustomerPhone());
                resultNode.put("customerAddress", order.getCustomerAddress());
                resultNode.put("totalPrice", order.getAmount());
                resultNode.put("paymentLink", order.getPayment_link());
                resultNode.put("orderStatus", order.getStatus().name());

                ArrayNode productArray = resultNode.putArray("products");
                List<OrderDetail> orderDetailList = orderDetailRepository.findAllOrderDetailByOrderID(OrderID);
                for (OrderDetail orderDetail : orderDetailList) {
                    ObjectNode productNode = JsonNodeFactory.instance.objectNode();
                    productNode.put("productId", orderDetail.getProduct().getProductID());
                    productNode.put("productName", orderDetail.getProduct().getProductName());
                    productNode.put("price", orderDetail.getProduct().getPrice());
                    productArray.add(productNode);
                }
            } else {
                resultNode.put("error", "Order not found");
            }
            return resultNode;
        } catch (Exception ex){
            ex.printStackTrace();
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @Override
    public Order findOrderByID(Integer orderID) {
        return orderRepository.findById(orderID)
                .map(order -> {
                    return order;
                })
                .orElse(new Order());
    }

    @Override
    public ObjectNode getAllOrder() {
//        return orderRepository.findAll()
//                .stream()
//                .map(order -> {
//                    List<OrderDetail> orderDetailList = orderDetailRepository.findAllOrderDetailByOrderID(order.getId());
//                    List<Product> productList = orderDetailList
//                            .stream()
//                            .map(OrderDetail::getProduct)
//                            .collect(Collectors.toList());
//                    return createOrderResponse(order, productList);
//                })
//                .collect(Collectors.toList());
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            List<Order> orderList = orderRepository.getAllOrder();
            ObjectNode resultNode = JsonNodeFactory.instance.objectNode();
            ArrayNode orderArray = resultNode.putArray("orders");
            for(Order order : orderList) {
                if (order != null) {
                    ObjectNode orderNode = JsonNodeFactory.instance.objectNode();
                    orderNode.put("id", order.getId());
                    orderNode.put("customerName", order.getCustomerName());
                    orderNode.put("customerEmail", order.getCustomerEmail());
                    orderNode.put("customerPhone", order.getCustomerPhone());
                    orderNode.put("customerAddress", order.getCustomerAddress());
                    orderNode.put("totalPrice", order.getAmount());
                    orderNode.put("paymentLink", order.getPayment_link());
                    orderNode.put("orderStatus", order.getStatus().name());

                    ArrayNode productArray = orderNode.putArray("products");
                    List<OrderDetail> orderDetailList = orderDetailRepository.findAllOrderDetailByOrderID(order.getId());
                    for (OrderDetail orderDetail : orderDetailList) {
                        ObjectNode productNode = JsonNodeFactory.instance.objectNode();
                        productNode.put("productId", orderDetail.getProduct().getProductID());
                        productNode.put("productName", orderDetail.getProduct().getProductName());
                        productNode.put("price", orderDetail.getProduct().getPrice());
                        productArray.add(productNode);
                    }
                    orderArray.add(orderNode);
                } else {
                    resultNode.put("error", "Order not found");
                }
            }
            return resultNode;
        } catch (Exception ex){
            ex.printStackTrace();
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @Override
    public List<OrderResponse> viewOrderHistoryCustomer(int customerId) {
        List<OrderResponse> orderResponseList = orderRepository.viewOrderHistoryCustomer(customerId)
                .stream()
                .map(order -> {
                    List<OrderDetail> orderDetailList = orderDetailRepository.findAllOrderDetailByOrderID(order.getId());
                    List<Product> productList = orderDetailList.stream()
                            .map(orderDetail -> productRepository.findById(orderDetail.getProduct().getProductID()).orElse(null))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    return createOrderResponse(order, productList);
                })
                .collect(Collectors.toList());
        return orderResponseList;
    }

    private OrderResponse createOrderResponse(Order newestOrder1, List<Product> productList)
    {
        return OrderResponse.builder()
                .orderID(newestOrder1.getId())
                .customerID(newestOrder1.getCustomer().getCustomerID())
                .customerPhone(newestOrder1.getCustomerPhone())
                .customerName(newestOrder1.getCustomerName())
                .customerEmail(newestOrder1.getCustomerEmail())
                .customerAddress(newestOrder1.getCustomerAddress())
                .note(newestOrder1.getDescription())
                .orderAmount(newestOrder1.getAmount())
                .productList(
                        productList.stream()
                            .map(this::mapperedToProductResponse)
                            .collect(Collectors.toList())
                )
                .OrderStatus(newestOrder1.getStatus().name())
                .build();
    }

    private ProductResponse mapperedToProductResponse(Product product)
    {
        return ProductResponse.builder()
                .productID(product.getProductID())
                .productName(product.getProductName())
                .price(product.getPrice())
                .description(product.getDescription())
                .typeOfProduct(product.getTypeOfProduct())
                .images(
                        imageRepository.findImageByProductID(product.getProductID())
                                .stream()
                                .map(Image::getImageUrl)
                                .collect(Collectors.toList())
                )
                .feedback(product.getFeedback())
                .productStatus(product.getProductStatus())
                .rating(product.getRating())
                .quantity(product.getQuantity())
                .build();
    }

    @Transactional
    @Override
    public MessageResponse DeleteOrder(Integer OrderID) {
        Optional<Order> order = orderRepository.findById(OrderID);

        if(order.isEmpty())
        {
            return new MessageResponse("Fail");
        }
        Customer customer = order.get().getCustomer();
        User user = userRepository.findById(customer.getCustomerID()).get();

        if(user.getAccountStatus() == AccountStatus.INACTIVE)
        {
            return new MessageResponse("Fail");
        }

        List<OrderDetail> orderDetailList = orderDetailRepository.findAllOrderDetailByOrderID(OrderID);
        if(orderDetailList.size() == 0)
        {
            return new MessageResponse("Fail");
        }

        List<Product> productList = orderDetailList
                    .stream()
                    .map(OrderDetail::getProduct)
                    .collect(Collectors.toList());

        // Update the status of Product
        productList.forEach(
                product -> {
                    product.setProductStatus(ProductStatus.AVAILABLE);
                    productRepository.save(product);
                }
        );

        orderVoucherRepository.deleteOrderVoucherByOrderID(OrderID);

        orderRepository.deleteOrderByOrderID(OrderID);

        int numberCancleOrder = customer.getNumberCancleOrder();
        String message = "Success";
        if(numberCancleOrder + 1 == 3)
        {
            message = "Cancel more than 3 times. Your account will be banned";
        }
        else if(numberCancleOrder + 1 > 3)
        {
            message = "Your account has been Banned";
            user.setAccountStatus(AccountStatus.INACTIVE);
            userRepository.save(user);
        }
        SendMailOrderRequest sendMailOrderRequest = new SendMailOrderRequest(
                order.get().getCustomer().getCustomerID(),
                order.get().getCustomer().getUser().getEmail(),
                order.get().getId());
        mailService.sendMailForCancelOrder(sendMailOrderRequest);
        customer.setNumberCancleOrder(numberCancleOrder + 1);
        customerRepository.save(customer);

        return new MessageResponse(message);
    }
}
